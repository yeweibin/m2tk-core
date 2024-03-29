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

    public static void checkIndex(int index, int limit)
    {
        if (index < 0 || index >= limit)
            throw new IndexOutOfBoundsException(String.format("index: %d, limit: %d", index, limit));
    }

    public static void checkFromToIndex(int fromIndex, int toIndex, int limit)
    {
        if (fromIndex < 0 || fromIndex > toIndex || toIndex > limit)
            throw new IndexOutOfBoundsException(String.format("fromIndex: %d, toIndex: %d, limit: %d", fromIndex, toIndex, limit));
    }

    public static void checkFromIndexSize(int fromIndex, int size, int limit)
    {
        if ((limit | fromIndex | size) < 0 || size > limit - fromIndex)
            throw new IndexOutOfBoundsException(String.format("fromIndex: %d, size: %d, limit: %d", fromIndex, size, limit));
    }
}
