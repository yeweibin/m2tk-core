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

public class SatelliteDeliverySystemDescriptorDecoder extends DescriptorDecoder
{
    public SatelliteDeliverySystemDescriptorDecoder()
    {
        super("SatelliteDeliverySystemDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.readUINT8(0) == 0x43);
    }

    public long getFrequencyCode()
    {
        return encoding.readUINT32(2);
    }

    public int getOrbitalPositionCode()
    {
        return encoding.readUINT16(6);
    }

    public int getWestEastFlag()
    {
        return (encoding.readUINT8(8) >> 7) & 0b1;
    }

    public int getPolarizationCode()
    {
        return (encoding.readUINT8(8) >> 5) & 0b11;
    }

    public int getModulationType()
    {
        return encoding.readUINT8(8) & 0x1F;
    }

    public int getSymbolRateCode()
    {
        return (int) ((encoding.readUINT32(9) >> 4) & 0x0FFFFFFF);
    }

    public int getInnerFECScheme()
    {
        return encoding.readUINT8(12) & 0b1111;
    }
}
