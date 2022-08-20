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

import m2tk.dvb.ServiceIdentifier;
import m2tk.encoding.Encoding;
import m2tk.encoding.IncorrectEncodingException;
import m2tk.mpeg2.decoder.DescriptorDecoder;

import java.util.Arrays;

public class AnnouncementSupportDescriptorDecoder extends DescriptorDecoder
{
    public AnnouncementSupportDescriptorDecoder()
    {
        super("AnnouncementSupportDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == 0x6E;
    }

    public int getAnnouncementSupportIndicator()
    {
        return encoding.readUINT16(2);
    }

    public int[] getSupportedAnnouncementTypeList()
    {
        int[] types = new int[8]; // 最多支持8种类型。
        int count = 0;
        int indicator = encoding.readUINT16(2) & 0xFF;
        for (int i = 0; i < types.length; i++)
        {
            int flag = (indicator >> i) & 0b1;
            if (flag == 1)
                types[count++] = i;
        }
        return Arrays.copyOf(types, count);
    }

    public int getReferenceType(int announcementType)
    {
        if (announcementType < 0 || announcementType > 7)
            throw new IllegalArgumentException("invalid announcement type: " + announcementType);
        int indicator = encoding.readUINT16(2) & 0xFF;
        int flag = (indicator >> announcementType) & 0b1;
        if (flag == 0)
            throw new IllegalArgumentException("unsupported announcement type: " + announcementType);

        int from = 4;
        int to = encoding.size();
        while (from < to)
        {
            int announcement_type = (encoding.readUINT8(from) >> 4) & 0b1111;
            int reference_type = encoding.readUINT8(from) & 0b111;
            if (announcement_type == announcementType)
                return reference_type;

            from += 1;
            if (reference_type == 0x01 || reference_type == 0x02 || reference_type == 0x03)
                from += 7;
        }
        throw new IncorrectEncodingException("no such value");
    }

    public ServiceIdentifier getAnnouncementServiceIdentifier(int announcementType)
    {
        if (announcementType < 0 || announcementType > 7)
            throw new IllegalArgumentException("invalid announcement type: " + announcementType);
        int indicator = encoding.readUINT16(2) & 0xFF;
        int flag = (indicator >> announcementType) & 0b1;
        if (flag == 0)
            throw new IllegalArgumentException("unsupported announcement type: " + announcementType);

        int from = 4;
        int to = encoding.size();
        while (from < to)
        {
            int announcement_type = (encoding.readUINT8(from) >> 4) & 0b1111;
            int reference_type = encoding.readUINT8(from) & 0b111;
            from += 1;

            if (announcement_type == announcementType &&
                (reference_type == 0x01 || reference_type == 0x02 || reference_type == 0x03))
            {
                ServiceIdentifier identifier = new ServiceIdentifier();
                identifier.original_network_id = encoding.readUINT16(from);
                identifier.transport_stream_id = encoding.readUINT16(from + 2);
                identifier.service_id = encoding.readUINT16(from + 4);
                identifier.component_tag = encoding.readUINT8(from + 6);
                return identifier;
            }

            if (reference_type == 0x01 || reference_type == 0x02 || reference_type == 0x03)
                from += 7;
        }
        throw new IncorrectEncodingException("no such value");
    }
}
