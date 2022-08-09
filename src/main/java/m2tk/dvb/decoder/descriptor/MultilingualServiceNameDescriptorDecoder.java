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

public class MultilingualServiceNameDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x5D;

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) &&
                target.readUINT8(0) == 0x5D);
    }

//    public MultilingualContent[] getServiceProviderNameList()
//    {
//        ArrayList<MultilingualContent> list = new ArrayList<>();
//        int from = 2;
//        int to   = encoding.size();
//        while (from < to)
//        {
//            int code = encoding.readUINT24(from);
//            byte[] chars = encoding.getRange(from + 4, encoding.readUINT8(from + 3));
//
//            MultilingualContent name = new MultilingualContent();
//            name.language = DVBUtils.decodeLanguageCode(code);
//            name.text = DVBUtils.decodeString(chars);
//
//            list.add(name);
//            from += 5 + chars.length + encoding.readUINT8(from + 4 + chars.length);
//        }
//        return list.toArray(new MultilingualContent[list.size()]);
//    }
//
//    public MultilingualContent[] getServiceNameList()
//    {
//        ArrayList<MultilingualContent> list = new ArrayList<>();
//        int from = 2;
//        int to   = encoding.size();
//        while (from < to)
//        {
//            int code = encoding.readUINT24(from);
//            int start = from + 4 + encoding.readUINT8(from + 3);
//            int finish = start + 1 + encoding.readUINT8(start);
//            byte[] chars = encoding.getRange(start + 1, finish);
//
//            MultilingualContent name = new MultilingualContent();
//            name.language = DVBUtils.decodeLanguageCode(code);
//            name.text = DVBUtils.decodeString(chars);
//
//            list.add(name);
//            from = finish;
//        }
//        return list.toArray(new MultilingualContent[list.size()]);
//    }
}
