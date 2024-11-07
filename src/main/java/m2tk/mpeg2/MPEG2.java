/*
 * Copyright (c) M2TK Project. All rights reserved.
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

package m2tk.mpeg2;

public final class MPEG2
{
    private MPEG2()
    {
    }

    public static final int TS_PACKET_SIZE = 188;
    public static final int TS_PACKET_HEADER_SIZE = 4;
    public static final int TS_PACKET_PAYLOAD_SIZE = 184;
    public static final int TS_PACKET_BIT_SIZE = MPEG2.TS_PACKET_SIZE * Byte.SIZE;
    public static final int TS_SYNC_BYTE = 0x47;
    public static final int TS_STUFFING_BYTE = 0xFF;
    public static final int RS_CODE_SIZE = 16; // 188 + 16 = 204 (Reed-Solomon Code)
    public static final int NULL_PACKET_PID = 0x1FFF;
    public static final int INVALID_PID = NULL_PACKET_PID + 1;
    public static final int PID_MASK = 0x1FFF;
    public static final int MIN_PID = 0x0000;
    public static final int MAX_PID = 0x1FFF;
    public static final int MIN_CCT = 0;
    public static final int MAX_CCT = 15;
    public static final int MIN_NETWORK_ID = 0x0000;
    public static final int MAX_NETWORK_ID = 0xFFFF;
    public static final int RESERVED_PROGRAM_NUMBER = 0x0000;   // '0' reserved for indicating network info
    public static final int MIN_PROGRAM_NUMBER = 0x0001;
    public static final int MAX_PROGRAM_NUMBER = 0xFFFF;
    public static final int MIN_TS_ID = 0x0000;
    public static final int MAX_TS_ID = 0xFFFF;
    public static final int MIN_DESCRIPTOR_TAG = 0x00;
    public static final int MAX_DESCRIPTOR_TAG = 0xFF;
    public static final int DESCRIPTOR_HEADER_LENGTH = 2;
    public static final int MAX_DESCRIPTOR_LENGTH = 257;
    public static final int MIN_DESCRIPTOR_PAYLOAD_LENGTH = 0;
    public static final int MAX_DESCRIPTOR_PAYLOAD_LENGTH = 255;
    public static final int SECTION_HEADER_LENGTH = 3;
    public static final int EXTENDED_SECTION_HEADER_LENGTH = 8;
    public static final int SECTION_PAYLOAD_START = 3;
    public static final int EXTENDED_SECTION_PAYLOAD_START = 8;
    public static final int CHECKSUM_LENGTH = 4;
    public static final int MIN_PSI_SECTION_LENGTH = 12;
    public static final int MAX_PSI_SECTION_LENGTH = 1024;
    public static final int MIN_PSI_SECTION_PAYLOAD_LENGTH = 9;
    public static final int MAX_PSI_SECTION_PAYLOAD_LENGTH = 1021;
    public static final int MIN_PRIVATE_SECTION_LENGTH = 3;
    public static final int MAX_PRIVATE_SECTION_LENGTH = 4096;
    public static final int MIN_PRIVATE_SECTION_PAYLOAD_LENGTH = 0;
    public static final int MAX_PRIVATE_SECTION_PAYLOAD_LENGTH = 4093;
    public static final int PES_PACKET_HEADER_LENGTH = 6;
    public static final int MAX_PES_PACKET_DATA_LENGTH = 65535; // 2^16 - 1
    public static final int MAX_PES_PACKET_LENGTH = PES_PACKET_HEADER_LENGTH + MAX_PES_PACKET_DATA_LENGTH;

    public static final int USER_PRIVATE_STREAM_TYPE = 0x80;
    public static final int MIN_TABLE_ID = 0x00;
    public static final int MAX_TABLE_ID = 0xFF;
    public static final int SYSTEM_CLOCK_FREQUENCY_HZ = 27_000_000;
    public static final int SYSTEM_CLOCK_FREQUENCY_MHZ = 27;

    // Stream Types
    public static final int RESERVED_STREAM_TYPE = 0x00;
    public static final int STREAM_TYPE_MPEG1_VIDEO = 0x01;
    public static final int STREAM_TYPE_MPEG2_VIDEO = 0x02;
    public static final int STREAM_TYPE_MPEG1_AUDIO = 0x03;
    public static final int STREAM_TYPE_MPEG2_AUDIO = 0x04;
    public static final int STREAM_TYPE_MPEG2_PRIVATE_SECTION = 0x05;
    public static final int STREAM_TYPE_MPEG2_PES_PRIVATE_DATA = 0x06;
    public static final int STREAM_TYPE_MHEG = 0x07;
    public static final int STREAM_TYPE_MPEG2_DSMCC = 0x08;
    public static final int STREAM_TYPE_MPEG2_DSMCC_TYPE_A = 0x0A;
    public static final int STREAM_TYPE_MPEG2_DSMCC_TYPE_B = 0x0B;
    public static final int STREAM_TYPE_MPEG2_DSMCC_TYPE_C = 0x0C;
    public static final int STREAM_TYPE_MPEG2_DSMCC_TYPE_D = 0x0D;
}
