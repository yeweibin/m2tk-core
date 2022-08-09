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
    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) &&
                target.readUINT8(0) == 0x54);
    }

//    public ContentClassification[] getContentClassificationList()
//    {
//        int count = (encoding.size() - 2) / 2;
//        ContentClassification[] list = new ContentClassification[count];
//        for (int i = 0; i < count; i ++)
//        {
//            ContentClassification classification = new ContentClassification();
//            int nibble = encoding.readUINT8(2 + i * 2);
//            classification.level1 = nibble >> 4;
//            classification.level2 = nibble & 0x0F;
//            classification.userDefined = encoding.readUINT8(2 + i * 2 + 1);
//            list[i] = classification;
//        }
//        return list;
//    }
}
