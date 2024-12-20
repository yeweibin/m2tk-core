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

package m2tk.dvb.decoder.section;

import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;
import m2tk.mpeg2.decoder.SectionDecoder;

public class RSTSectionDecoder extends SectionDecoder
{
    public RSTSectionDecoder()
    {
        super(RSTSectionDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) &&
                target.readUINT8(0) == 0x71 &&
                target.size() <= MPEG2.MAX_PSI_SECTION_LENGTH &&
                (target.size() - 3) % 9 == 0);
    }

    public int getRunningStatusCount()
    {
        return getSectionLength() / 9;
    }

    public int getTransportStreamID(int index)
    {
        int offset = 3 + 9 * index;
        return encoding.readUINT16(offset);
    }

    public int getOriginalNetworkID(int index)
    {
        int offset = 3 + 9 * index;
        return encoding.readUINT16(offset + 2);
    }

    public int getServiceID(int index)
    {
        int offset = 3 + 9 * index;
        return encoding.readUINT16(offset + 4);
    }

    public int getEventID(int index)
    {
        int offset = 3 + 9 * index;
        return encoding.readUINT16(offset + 6);
    }

    public int getRunningStatus(int index)
    {
        int offset = 3 + 9 * index;
        return encoding.readUINT8(offset + 8) & 0b111;
    }
}
