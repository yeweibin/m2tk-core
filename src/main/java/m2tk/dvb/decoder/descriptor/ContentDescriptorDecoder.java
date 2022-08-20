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
import m2tk.mpeg2.decoder.DescriptorDecoder;

public class ContentDescriptorDecoder extends DescriptorDecoder
{
    public ContentDescriptorDecoder()
    {
        super("ContentDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == 0x54;
    }

    public int[] getContentIdentifierList()
    {
        int count = getPayloadLength() / 2;
        int[] list = new int[count];
        for (int i = 0; i < count; i++)
            list[i] = encoding.readUINT16(2 + i * 2);
        return list;
    }

    public int getContentNibbleLevel1(int identifier)
    {
        return (identifier >> 12) & 0xF;
    }

    public int getContentNibbleLevel2(int identifier)
    {
        return (identifier >> 8) & 0xF;
    }

    public int getUserByte(int identifier)
    {
        return identifier & 0xFF;
    }
}
