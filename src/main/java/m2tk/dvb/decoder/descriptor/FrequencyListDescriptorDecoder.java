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

import m2tk.encoding.Encoding;
import m2tk.mpeg2.decoder.DescriptorDecoder;

public class FrequencyListDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x62;

    public FrequencyListDescriptorDecoder()
    {
        super("FrequencyListDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == TAG;
    }

    public int getCodingType()
    {
        return encoding.readUINT8(2) & 0b11;
    }

    public long[] getFrequencyList()
    {
        long[] list = new long[(encoding.size() - 3) / 4];
        for (int i = 0; i < list.length; i ++)
        {
            list[i] = encoding.readUINT32(3 + i * 4);
        }
        return list;
    }
}
