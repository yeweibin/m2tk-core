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

public class OneMultilingualTextDecoder extends Decoder
{
    public OneMultilingualTextDecoder()
    {
        super("OneMultilingualTextDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.size() >= 4);
    }

    public String getLanguageCode()
    {
        int languageCode = encoding.readUINT24(0);
        return DVB.decodeThreeLetterCode(languageCode);
    }

    public String getText()
    {
        byte[] chars = encoding.getRange(3, encoding.size());
        return DVB.decodeString(chars);
    }
}
