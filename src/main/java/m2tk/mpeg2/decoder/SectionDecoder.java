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

import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;

public class SectionDecoder extends Decoder
{
    public SectionDecoder()
    {
        super(SectionDecoder.class.getSimpleName());
    }

    protected SectionDecoder(String name)
    {
        super(name);
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        if (!super.isAttachable(target))
            return false;

        int size = target.size();
        return (size >= MPEG2.SECTION_HEADER_LENGTH &&
                size <= MPEG2.MAX_PRIVATE_SECTION_LENGTH &&
                size == MPEG2.SECTION_HEADER_LENGTH + (target.readUINT16(1) & 0x0FFF));
    }

    public int getTableID()
    {
        return encoding.readUINT8(0);
    }

    public int getSyntaxIndicator()
    {
        return (encoding.readUINT8(1) >> 7) & 0b1;
    }

    public int getPrivateIndicator()
    {
        return (encoding.readUINT8(1) >> 6) & 0b1;
    }

    public int getSectionLength()
    {
        return encoding.readUINT16(1) & 0xFFF;
    }

    public Encoding getPayload()
    {
        return encoding.readSelector(3);
    }
}
