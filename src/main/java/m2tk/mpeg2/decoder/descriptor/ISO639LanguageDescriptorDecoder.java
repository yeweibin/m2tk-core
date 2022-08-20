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

package m2tk.mpeg2.decoder.descriptor;

import m2tk.dvb.DVB;
import m2tk.encoding.Encoding;
import m2tk.mpeg2.decoder.DescriptorDecoder;

public class ISO639LanguageDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x0A;
    private static final int BLOCK_SIZE = 4;

    public ISO639LanguageDescriptorDecoder()
    {
        super("ISO639LanguageDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) &&
               target.readUINT8(0) == TAG &&
               target.readUINT8(1) % BLOCK_SIZE == 0;
    }

    public int getDescriptionCount()
    {
        return getPayloadLength() / BLOCK_SIZE;
    }

    public String getLanguageCode(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return DVB.decodeThreeLetterCode(encoding.readUINT24(offset));
    }

    public int getAudioType(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT8(offset + 3);
    }
}
