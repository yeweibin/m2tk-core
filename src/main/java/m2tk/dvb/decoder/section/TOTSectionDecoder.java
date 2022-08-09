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
import m2tk.mpeg2.decoder.SectionDecoder;

public class TOTSectionDecoder extends SectionDecoder
{
    public TOTSectionDecoder()
    {
        super(TOTSectionDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) &&
                target.readUINT8(0) == 0x73 &&
                target.size() >= 14 && target.size() <= MPEG2.MAX_PSI_SECTION_LENGTH);
    }

    public long getUTCTime()
    {
        return encoding.readUINT40(3);
    }

    public Encoding getDescriptorLoop()
    {
        int len = encoding.readUINT16(8) & 0xFFF;
        return encoding.readSelector(10, len);
    }

    public long getChecksum()
    {
        return encoding.readUINT32(encoding.size() - MPEG2.CHECKSUM_LENGTH);
    }

    public boolean isChecksumCorrect()
    {
        return (encoding.checksum() == 0);
    }
}
