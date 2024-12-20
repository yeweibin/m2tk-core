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

public class DescriptorDecoder extends Decoder
{
    public DescriptorDecoder()
    {
        super("DescriptorDecoder");
    }

    protected DescriptorDecoder(String name)
    {
        super(name);
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        if (!super.isAttachable(target))
            return false;

        int size = target.size();
        return size == MPEG2.DESCRIPTOR_HEADER_LENGTH + target.readUINT8(1);
    }

    public int getTag()
    {
        return encoding.readUINT8(0);
    }

    public int getPayloadLength()
    {
        return encoding.readUINT8(1);
    }

    public Encoding getPayload()
    {
        return encoding.readSelector(2);
    }
}
