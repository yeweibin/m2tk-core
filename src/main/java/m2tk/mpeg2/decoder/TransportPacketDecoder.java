/*
 * Copyright (c) Ye Weibin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package m2tk.mpeg2.decoder;

import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;

public class TransportPacketDecoder extends Decoder
{
    public TransportPacketDecoder()
    {
        super(TransportPacketDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.size() == MPEG2.TS_PACKET_SIZE;
    }

    public int getSyncByte()
    {
        return encoding.readUINT8(0);
    }

    public int getTransportErrorIndicator()
    {
        return (encoding.readUINT8(1) >> 7) & 0b1;
    }

    public int getPayloadUnitStartIndicator()
    {
        return (encoding.readUINT8(1) >> 6) & 0b1;
    }

    public int getTransportPriority()
    {
        return (encoding.readUINT8(1) >> 5) & 0b1;
    }

    public int getPID()
    {
        return encoding.readUINT16(1) & MPEG2.PID_MASK;
    }

    public int getTransportScramblingControl()
    {
        return (encoding.readUINT8(3) >> 6) & 0b11;
    }

    public int getAdaptationFieldControl()
    {
        return (encoding.readUINT8(3) >> 4) & 0b11;
    }

    public int getContinuityCounter()
    {
        return encoding.readUINT8(3) & 0b1111;
    }

    public int getNextContinuityCounter()
    {
        int cct = getContinuityCounter();
        return (cct + 1) & 0b1111;
    }

    public int getPreviousContinuityCounter()
    {
        int cct = getContinuityCounter();
        return (cct - 1) & 0b1111;
    }

    public boolean containsTransportError()
    {
        int indicator = getTransportErrorIndicator();
        return (indicator == 1);
    }

    public boolean containsPayloadUnitStartByte()
    {
        int indicator = getPayloadUnitStartIndicator();
        return (indicator == 1);
    }

    public boolean containsAdaptationField()
    {
        int control = getAdaptationFieldControl();
        return (control == 0b10 || control == 0b11);
    }

    public boolean containsUsefulAdaptationField()
    {
        int control = getAdaptationFieldControl();
        if (control == 0b00 || control == 0b01)
            return false;

        // length??????183????????????????????????????????????????????????
        int length = encoding.readUINT8(4);
        return (0 < length && length < 184);
    }

    public boolean containsPayload()
    {
        int control = getAdaptationFieldControl();
        return (control == 0b01 || control == 0b11);
    }

    public boolean isScrambled()
    {
        int control = getTransportScramblingControl();
        return (control != 0b00);
    }

    public int getPayloadStartPosition()
    {
        // ??????????????????????????????
        // ???????????? PSI ???????????????????????????????????? pointer_field???
        return containsAdaptationField()
               ? MPEG2.TS_PACKET_HEADER_SIZE + getAdaptationFieldLength()
               : MPEG2.TS_PACKET_HEADER_SIZE;
    }

    public int getAdaptationFieldLength()
    {
        return containsAdaptationField()
               ? 1 + encoding.readUINT8(4)
               : 0;
    }

    public Encoding getAdaptationField()
    {
        if (!containsAdaptationField())
            throw new UnsupportedOperationException("no such field");
        int length = getAdaptationFieldLength();
        return encoding.readSelector(4, length); // ??????length??????
    }

    public Encoding getPayload()
    {
        if (!containsPayload())
            throw new UnsupportedOperationException("no such field");
        int start = getPayloadStartPosition();
        return encoding.readSelector(start);
    }

    ////////////////////////////////////////////////////////////////////

    public static final byte[] PACKET_MASK_IGNORE_PCR =
    {
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, // 33-bit PCR_base
        (byte) 0x00, (byte) 0x00, (byte) 0x7E, (byte) 0x00, // 6-bit reserve, 9-bit PCR_ext
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
    };

    public static final byte[]  NULL_PACKET =
    {
        (byte) 0x47, (byte) 0x1F, (byte) 0xFF, (byte) 0x1F,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
    };

//    /**
//     * ???????????????????????????????????????????????????????????????
//     *
//     * <p>ISO.13818-1????????????????????????????????????
//     * <p>???1?????????????????????PID??????????????????2???????????????3????????????????????????????????????4????????????CCT??????
//     * ??????5??????????????????????????????'01'???'11'??????6????????????????????????????????????????????????PCR????????????
//     * PCR?????????????????????????????????????????????????????????????????????(7)??????????????????????????????????????????
//     * ??????PCR???????????????????????????????????????????????????????????????
//     * <p>?????????????????????????????????????????????????????????????????????
//     * <p>??????????????????transport_error_indicator??????'1'????????????????????????????????????????????????????????????
//     *
//     * @param packet1 ??????????????????
//     * @param packet2 ??????????????????
//     * @return ????????????????????????????????????????????????
//     */
//    public static boolean isDuplicate(byte[] packet1, byte[] packet2)
//    {
//        if (TransportPacket.getTransportErrorIndicator(packet1) == 1 ||
//            TransportPacket.getTransportErrorIndicator(packet2) == 1)
//            return false;
//
//        int pid1 = TransportPacket.getPID(packet1);
//        int pid2 = TransportPacket.getPID(packet2);
//        if (pid1 == MPEG2.NULL_PACKET_PID ||
//            pid2 == MPEG2.NULL_PACKET_PID ||
//            pid2 != pid1)
//            return false;
//
//        int cct1 = TransportPacket.getCCT(packet1);
//        int cct2 = TransportPacket.getCCT(packet2);
//        if (cct2 != cct1)
//            return false;
//
//        int afc1 = TransportPacket.getAdaptationFieldControl(packet1);
//        int afc2 = TransportPacket.getAdaptationFieldControl(packet2);
//        if (afc1 == 0b00 || afc1 == 0b10 ||     // ?????????????????????????????????CCT?????????????????????????????????
//            afc2 == 0b00 || afc2 == 0b10 ||     // ??????
//            afc1 != afc2)                       // ?????????????????????????????????????????????
//            return false;
//
//        AdaptationField af2 = getAdaptationField(packet2);
//        return (afc2 == 0x01 || af2.getProgramClockReferenceFlag() == 0)
//            ? Arrays.equals(packet1, packet2)   // ?????????????????????????????????PCR
//            : bytes_equals_with_mask(packet1, packet2, PACKET_MASK_IGNORE_PCR); // ??????PCR??????????????????
//    }
}