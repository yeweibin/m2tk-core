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

public class LinkageDescriptorDecoder extends DescriptorDecoder
{
    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) &&
                target.readUINT8(0) == 0x4A);
    }

    public int getTransportStreamID()
    {
        return encoding.readUINT16(2);
    }

    public int getOriginalNetworkID()
    {
        return encoding.readUINT16(4);
    }

    public int getServiceID()
    {
        return encoding.readUINT16(6);
    }

    public int getLinkageType()
    {
        return encoding.readUINT8(8);
    }

    public Encoding getMobileHandoverInfo()
    {
        if (getLinkageType() != 0x08)
            return encoding.readSelector(9, 0);

        int handoverType = (encoding.readUINT8(9) >> 4) & 0xF;
        int originType = encoding.readUINT8(9) & 0b1;
        int len = 1;
        if (handoverType == 0x01 || handoverType == 0x02 || handoverType == 0x03)
            len += 2; // 包含network_id
        if (originType == 0x00)
            len += 2; // 包含initial_service_id

        return encoding.readSelector(9, len);
    }

    public int getNetworkID(Encoding handover)
    {
        int handoverType = (handover.readUINT8(0) >> 4) & 0xF;
        return (handoverType == 0x01 || handoverType == 0x02 || handoverType == 0x03)
               ? handover.readUINT16(1) // 包含network_id
               : -1;
    }

    public int getInitialServiceID(Encoding handover)
    {
        int handoverType = (handover.readUINT8(0) >> 4) & 0xF;
        int originType = handover.readUINT8(0) & 0b1;
        int offset = 1;
        if (handoverType == 0x01 || handoverType == 0x02 || handoverType == 0x03)
            offset += 2; // 包含network_id
        return (originType == 0x00)
               ? handover.readUINT16(offset) // 包含initial_service_id
               : -1;
    }

    public Encoding getEventLinkageInfo()
    {
        return (getLinkageType() == 0x0D)
               ? encoding.readSelector(9, 3)
               : encoding.readSelector(9, 0);
    }

    public Encoding getExtendedEventLinkageInfo()
    {
        return (getLinkageType() == 0x0E)
               ? encoding.readSelector(9, 1 + encoding.readUINT8(9))
               : encoding.readSelector(9, 0);
    }

    public Encoding getSelector()
    {
        return encoding.readSelector(9);
    }
}
