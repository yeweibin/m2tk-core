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
import m2tk.util.IntBiConsumer;

import java.util.Objects;

public class ServiceListDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x41;

    public ServiceListDescriptorDecoder()
    {
        super("ServiceListDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == TAG;
    }

    public int[] getServiceIDList()
    {
        int[] list = new int[(encoding.size() - 2) / 3];
        for (int i = 0; i < list.length; i ++)
        {
            list[i] = encoding.readUINT16(2 + i * 3);
        }
        return list;
    }

    public int[] getServiceTypeList()
    {
        int[] list = new int[(encoding.size() - 2) / 3];
        for (int i = 0; i < list.length; i ++)
        {
            list[i] = encoding.readUINT8(2 + i * 3 + 2);
        }
        return list;
    }

    public void forEach(IntBiConsumer consumer)
    {
        Objects.requireNonNull(consumer);
        for (int offset = 2; offset < encoding.size(); offset += 3)
        {
            int sid = encoding.readUINT16(offset);
            int type = encoding.readUINT8(2);
            consumer.accept(sid, type);
        }
    }
}
