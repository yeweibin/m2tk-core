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

package m2tk.dvb.decoder.section;

import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;
import m2tk.mpeg2.decoder.ExtendedSectionDecoder;
import m2tk.mpeg2.decoder.PSISectionDecoder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class EITSectionDecoder extends ExtendedSectionDecoder
{
    public EITSectionDecoder()
    {
        super(EITSectionDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        if (!super.isAttachable(target) || target.size() > MPEG2.MAX_PRIVATE_SECTION_LENGTH)
            return false;

        int tableId = target.readUINT8(0);
        return (tableId >= 0x4E && tableId <= 0x6F);
    }

    public int getServiceID()
    {
        return getTableIDExtension();
    }

    public int getTransportStreamID()
    {
        return encoding.readUINT16(8);
    }

    public int getOriginalNetworkID()
    {
        return encoding.readUINT16(10);
    }

    public int getSegmentLastSectionNumber()
    {
        return encoding.readUINT8(12);
    }

    public int getLastTableID()
    {
        return encoding.readUINT8(13);
    }

    public Encoding[] getEventDescriptionList()
    {
        ArrayList<Encoding> list = new ArrayList<>();
        int from = 14;
        int to = encoding.size() - MPEG2.CHECKSUM_LENGTH;
        while (from < to)
        {
            int length = 12 + (encoding.readUINT16(from + 10) & 0xFFF);
            list.add(encoding.readSelector(from, length));
            from += length;
        }
        return list.toArray(new Encoding[0]);
    }

    public void forEachEventDescription(Consumer<Encoding> consumer)
    {
        Objects.requireNonNull(consumer);
        int from = 14;
        int to = encoding.size() - MPEG2.CHECKSUM_LENGTH;
        while (from < to)
        {
            int length = 12 + (encoding.readUINT16(from + 10) & 0xFFF);
            Encoding description = encoding.readSelector(from, length);
            consumer.accept(description);
            from += length;
        }
    }
}
