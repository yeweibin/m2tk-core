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

import java.util.ArrayList;

public class MultilingualComponentDescriptorDecoder extends DescriptorDecoder
{
    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) &&
                target.readUINT8(0) == 0x5E);
    }

    public int getComponentTag()
    {
        return encoding.readUINT8(2);
    }

//    public MultilingualContent[] getDescriptionList()
//    {
//        ArrayList<MultilingualContent> list = new ArrayList<>();
//        int from = 3;
//        int to   = encoding.size();
//        while (from < to)
//        {
//            int code = encoding.readUINT24(from);
//            byte[] chars = encoding.getRange(from + 4, encoding.readUINT8(from + 3));
//
//            MultilingualContent description = new MultilingualContent();
//            description.language = DVBUtils.decodeLanguageCode(code);
//            description.text = DVBUtils.decodeString(chars);
//
//            list.add(description);
//            from += 4 + chars.length;
//        }
//        return list.toArray(new MultilingualContent[list.size()]);
//    }
}
