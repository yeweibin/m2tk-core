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

public class MultilingualBouquetNameDescriptorDecoder extends DescriptorDecoder
{
    public MultilingualBouquetNameDescriptorDecoder()
    {
        super(MultilingualBouquetNameDescriptorDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.readUINT8(0) == 0x5C);
    }

    public Encoding getMultilingualBouquetName(int index)
    {
        int idx = 0;
        int from = 2;
        int to   = encoding.size();
        while (from < to)
        {
            int block_size = 3 + 1 + encoding.readUINT8(from + 3);
            if (idx == index)
                return encoding.readSelector(from, block_size);

            from += block_size;
            idx ++;
        }
        throw new IndexOutOfBoundsException("invalid index: " + index);
    }
}
