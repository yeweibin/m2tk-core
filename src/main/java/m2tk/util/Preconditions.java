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

package m2tk.util;

public class Preconditions
{
    private Preconditions() {}

    public static void checkIndex(int index, int length)
    {
        if (index < 0 || index >= length)
            throw new IndexOutOfBoundsException(String.format("index: %d, length: %d", index, length));
    }

    public static void checkFromToIndex(int fromIndex, int toIndex, int length)
    {
        if (fromIndex < 0 || fromIndex > toIndex || toIndex > length)
            throw new IndexOutOfBoundsException(String.format("fromIndex: %d, toIndex: %d, length: %d", fromIndex, toIndex, length));
    }

    public static void checkFromIndexSize(int fromIndex, int size, int length)
    {
        if ((length | fromIndex | size) < 0 || size > length - fromIndex)
            throw new IndexOutOfBoundsException(String.format("fromIndex: %d, size: %d, length: %d", fromIndex, size, length));
    }
}
