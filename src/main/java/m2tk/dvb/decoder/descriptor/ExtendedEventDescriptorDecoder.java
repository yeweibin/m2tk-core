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
package m2tk.dvb.decoder.descriptor;

import m2tk.dvb.DVB;
import m2tk.encoding.Encoding;
import m2tk.mpeg2.decoder.DescriptorDecoder;

import java.util.ArrayList;
import java.util.List;

public class ExtendedEventDescriptorDecoder extends DescriptorDecoder
{
    public ExtendedEventDescriptorDecoder()
    {
        super(ExtendedEventDescriptorDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.readUINT8(0) == 0x4E);
    }

    public int getDescriptorNumber()
    {
        return (encoding.readUINT8(2) >> 4) & 0xF;
    }

    public int getLastDescriptorNumber()
    {
        return encoding.readUINT8(2) & 0xF;
    }

    public String getLanguageCode()
    {
        int code = encoding.readUINT24(3);
        return DVB.decodeThreeLetterCode(code);
    }

    public int getLengthOfItems()
    {
        return encoding.readUINT8(6);
    }

    public Encoding[] getItems()
    {
        int offset = 7;
        int limit = offset + getLengthOfItems();
        List<Encoding> list = new ArrayList<>();
        while (offset < limit)
        {
            int start = offset;
            offset += 1 + encoding.readUINT8(offset);
            offset += 1 + encoding.readUINT8(offset);
            list.add(encoding.readSelector(start, offset - start));
        }
        return list.toArray(new Encoding[0]);
    }

    public int getItemDescriptionLength(Encoding item)
    {
        return item.readUINT8(0);
    }

    public String getItemDescription(Encoding item)
    {
        int len = item.readUINT8(0);
        return DVB.decodeString(item.getRange(1, 1 + len));
    }

    public int getItemTextLength(Encoding item)
    {
        int offset = 1 + item.readUINT8(0);
        return item.readUINT8(offset);
    }

    public String getItemText(Encoding item)
    {
        int offset = 1 + item.readUINT8(0);
        int len = item.readUINT8(offset);
        return DVB.decodeString(item.getRange(offset + 1, offset + 1 + len));
    }

    public int getTextLength()
    {
        int offset = 7 + getLengthOfItems();
        return encoding.readUINT8(offset);
    }

    public String getText()
    {
        int offset = 7 + getLengthOfItems();
        int len = encoding.readUINT8(offset);
        return DVB.decodeString(encoding.getRange(offset + 1, offset + 1 + len));
    }
}
