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

import m2tk.dvb.DVB;
import m2tk.encoding.Encoding;
import m2tk.mpeg2.decoder.DescriptorDecoder;

public class ParentalRatingDescriptorDecoder extends DescriptorDecoder
{
    public static final int TAG = 0x55;

    public ParentalRatingDescriptorDecoder()
    {
        super("ParentalRatingDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == TAG;
    }

    public int getRatingCount()
    {
        return getPayloadLength() / 4;
    }

    public String getCountryCode(int index)
    {
        return DVB.decodeThreeLetterCode(encoding.readUINT24(2 + index * 4));
    }

    public int getRating(int index)
    {
        return encoding.readUINT8(2 + index * 4 + 3);
    }
}
