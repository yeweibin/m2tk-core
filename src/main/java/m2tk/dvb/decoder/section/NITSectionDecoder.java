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
import m2tk.mpeg2.decoder.PSISectionDecoder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class NITSectionDecoder extends PSISectionDecoder
{
    public NITSectionDecoder()
    {
        super(NITSectionDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        if (!super.isAttachable(target))
            return false;

        int tableId = target.readUINT8(0);
        return (tableId == 0x40 || tableId == 0x41);
    }

    public int getNetworkID()
    {
        return getTableIDExtension();
    }

    public Encoding getDescriptorLoop()
    {
        int len = encoding.readUINT16(8) & 0xFFF;
        return encoding.readSelector(10, len);
    }

    public Encoding[] getTransportStreamDescriptionList()
    {
        ArrayList<Encoding> list = new ArrayList<>();
        int from = 10 + (encoding.readUINT16(8) & 0xFFF) + 2;
        int to = encoding.size() - MPEG2.CHECKSUM_LENGTH;
        while (from < to)
        {
            int length = 6 + (encoding.readUINT16(from + 4) & 0xFFF);
            list.add(encoding.readSelector(from, length));
            from += length;
        }
        return list.toArray(new Encoding[0]);
    }

    public void forEachTransportStreamDescription(Consumer<Encoding> consumer)
    {
        Objects.requireNonNull(consumer);
        int from = 10 + (encoding.readUINT16(8) & 0xFFF) + 2;
        int to = encoding.size() - MPEG2.CHECKSUM_LENGTH;
        while (from < to)
        {
            int length = 6 + (encoding.readUINT16(from + 4) & 0xFFF);
            Encoding description = encoding.readSelector(from, length);
            consumer.accept(description);
            from += length;
        }
    }
}
