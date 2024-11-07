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

package m2tk.dvb.decoder.descriptor;

import m2tk.dvb.DVB;
import m2tk.encoding.Encoding;
import m2tk.mpeg2.decoder.DescriptorDecoder;

public class SubtitlingDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x59;
    private static final int BLOCK_SIZE = 8;

    public SubtitlingDescriptorDecoder()
    {
        super("SubtitlingDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) &&
               target.readUINT8(0) == TAG &&
               target.readUINT8(1) % BLOCK_SIZE == 0;
    }

    public int getSubtitlingDescriptionCount()
    {
        return getPayloadLength() / BLOCK_SIZE;
    }

    public Encoding[] getSubtitlingDescriptionList()
    {
        int count = getPayloadLength() / BLOCK_SIZE;
        Encoding[] list = new Encoding[count];
        for (int i = 0; i < count; i++)
            list[i] = encoding.readSelector(2 + i * BLOCK_SIZE, BLOCK_SIZE);
        return list;
    }

    public String getLanguageCode(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return DVB.decodeThreeLetterCode(encoding.readUINT24(offset));
    }

    public String getLanguageCode(Encoding description)
    {
        return DVB.decodeThreeLetterCode(description.readUINT24(0));
    }

    public int getSubtitlingType(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT8(offset + 3);
    }

    public int getSubtitlingType(Encoding description)
    {
        return description.readUINT8(3);
    }

    public int getCompositionPageID(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT16(offset + 4);
    }

    public int getAncillaryPageID(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT16(offset + 6);
    }

    public int getAncillaryPageID(Encoding description)
    {
        return description.readUINT16(6);
    }
}
