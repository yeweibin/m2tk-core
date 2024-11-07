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

public class ComponentDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x50;

    public ComponentDescriptorDecoder()
    {
        super("ComponentDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == TAG;
    }

    public int getStreamContentType()
    {
        return encoding.readUINT8(2) & 0x0F;
    }

    public int getComponentType()
    {
        return encoding.readUINT8(3);
    }

    public int getComponentTag()
    {
        return encoding.readUINT8(4);
    }

    public String getLanguageCode()
    {
        int code = encoding.readUINT24(5);
        return DVB.decodeThreeLetterCode(code);
    }

    public String getText()
    {
        byte[] chars = encoding.getRange(8, encoding.size());
        return DVB.decodeString(chars);
    }
}
