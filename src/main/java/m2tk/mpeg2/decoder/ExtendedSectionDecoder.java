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

package m2tk.mpeg2.decoder;

import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;

public class ExtendedSectionDecoder extends SectionDecoder
{
    public ExtendedSectionDecoder()
    {
        super(ExtendedSectionDecoder.class.getSimpleName());
    }

    protected ExtendedSectionDecoder(String name)
    {
        super(name);
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        if (!super.isAttachable(target))
            return false;

        int indicator = (target.readUINT8(1) >> 7) & 0b1;
        return (indicator == 1 &&
                target.size() >= MPEG2.MIN_PSI_SECTION_LENGTH);
    }

    public int getTableIDExtension()
    {
        return encoding.readUINT16(3);
    }

    public int getVersionNumber()
    {
        return (encoding.readUINT8(5) >> 1) & 0b11111;
    }

    public int getNextVersionNumber()
    {
        int version = getVersionNumber();
        return (version + 1) & 0b11111;
    }

    public int getPrevVersionNumber()
    {
        int version = getVersionNumber();
        return (version - 1) & 0b11111;
    }

    public int getCurrentNextIndicator()
    {
        return encoding.readUINT8(5) & 0b1;
    }

    public boolean isCurrentlyApplicable()
    {
        int indicator = getCurrentNextIndicator();
        return (indicator == 1);
    }

    public int getSectionNumber()
    {
        return encoding.readUINT8(6);
    }

    public int getLastSectionNumber()
    {
        return encoding.readUINT8(7);
    }

    public long getChecksum()
    {
        return encoding.readUINT32(encoding.size() - MPEG2.CHECKSUM_LENGTH);
    }

    public boolean isChecksumCorrect()
    {
        return (encoding.checksum() == 0);
    }

    @Override
    public Encoding getPayload()
    {
        return encoding.readSelector(8, encoding.size() - 12);
    }
}
