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

import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;
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

    public Encoding getLocalTimeOffsetDescription(int index)
    {
        int offset = MPEG2.DESCRIPTOR_HEADER_LENGTH + index * BLOCK_SIZE;
        return encoding.readSelector(offset, BLOCK_SIZE);
    }
}
