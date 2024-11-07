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

import java.util.ArrayList;
import java.util.List;

public class MultilingualServiceNameDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x5D;

    public MultilingualServiceNameDescriptorDecoder()
    {
        super("MultilingualServiceNameDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == TAG;
    }

    public Encoding[] getMultilingualNames()
    {
        List<Encoding> list = new ArrayList<>();
        int offset = 2;
        while (offset < encoding.size())
        {
            int start = offset;
            offset += 4 + encoding.readUINT8(offset + 3);
            offset += 1 + encoding.readUINT8(offset);
            list.add(encoding.readSelector(start, offset - start));
        }
        return list.toArray(new Encoding[0]);
    }

    public String getLanguageCode(Encoding name)
    {
        return DVB.decodeThreeLetterCode(name.readUINT24(0));
    }

    public String getMultilingualServiceProviderName(Encoding name)
    {
        int len = name.readUINT8(3);
        return DVB.decodeString(name.getRange(4, 4 + len));
    }

    public String getMultilingualServiceName(Encoding name)
    {
        int offset = 4 + name.readUINT8(3);
        int len = name.readUINT8(offset);
        return DVB.decodeString(name.getRange(offset + 1, offset + 1 + len));
    }
}
