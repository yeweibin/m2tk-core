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
package m2tk.dvb.decoder.element;

import m2tk.dvb.DVB;
import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;

public class TwoMultilingualTextDecoder extends Decoder
{
    public TwoMultilingualTextDecoder()
    {
        super("TwoMultilingualTextDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        if (!super.isAttachable(target))
            return false;

        int text1_length = target.readUINT8(3);
        int text2_length = target.readUINT8(3 + text1_length);
        return target.size() == (4 + text1_length + text2_length);
    }

    public String getLanguageCode()
    {
        int languageCode = encoding.readUINT24(0);
        return DVB.decodeThreeLetterCode(languageCode);
    }

    public String getText1()
    {
        int text1_length = encoding.readUINT8(3);
        byte[] chars = encoding.getRange(4, 4 + text1_length);
        return DVB.decodeString(chars);
    }

    public String getText2()
    {
        int text1_length = encoding.readUINT8(3);
        int text2_length = encoding.readUINT8(4 + text1_length);
        byte[] chars = encoding.getRange(4 + text1_length, 4 + text1_length + text2_length);
        return DVB.decodeString(chars);
    }
}
