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

package m2tk.encoding;

/**
 * 除了表明身份（Decoder），其余什么都不能做的基础类。<p>
 * Decoder就像一个模具，将编码框起来，对于不合规的编码给出错误提示。
 */
public abstract class Decoder
{
    private final String name;
    protected Encoding encoding;

    protected Decoder(String name)
    {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Bad decoder name: " + name);
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isAttachable(Encoding target)
    {
        return target != null;
    }

    public Encoding getEncoding()
    {
        return encoding;
    }

    public void attach(Encoding target)
    {
        encoding = target;
    }

    public void detach()
    {
        encoding = null;
    }

    @Override
    public String toString()
    {
        return (encoding == null)
               ? "Decoder{" + name + "}"
               : encoding.toHexString();
    }
}
