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

public class LocalTimeOffsetDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x58;
    private static final int BLOCK_SIZE = 13;

    public LocalTimeOffsetDescriptorDecoder()
    {
        super("LocalTimeOffsetDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == TAG;
    }

    public int getDescriptionCount()
    {
        return encoding.readUINT8(1) / BLOCK_SIZE;
    }

    public String getCountryCode(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return DVB.decodeThreeLetterCode(encoding.readUINT24(offset));
    }

    public int getCountryRegionID(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return (encoding.readUINT8(offset + 3) >> 2) & 0b111111;
    }

    public int getLocalTimeOffsetPolarity(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT8(offset + 3) & 0b1;
    }

    public int getLocalTimeOffset(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT16(offset + 4);
    }

    public long getTimeOfChange(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT40(offset + 6);
    }

    public int getNextTimeOffset(int index)
    {
        int offset = 2 + BLOCK_SIZE * index;
        return encoding.readUINT16(offset + 11);
    }
}
