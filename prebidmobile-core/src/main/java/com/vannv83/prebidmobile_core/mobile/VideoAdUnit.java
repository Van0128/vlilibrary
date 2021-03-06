/*
 *    Copyright 2018-2019 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vannv83.prebidmobile_core.mobile;

import androidx.annotation.NonNull;

public class VideoAdUnit extends AdUnit {

    private final AdSize adSize;
    private final PlacementType type;

    public VideoAdUnit(@NonNull String configId, int width, int height, PlacementType type) {
        super(configId, AdType.VIDEO);
        adSize = new AdSize(width, height);
        this.type = type;
    }

    AdSize getAdSize() {
        return adSize;
    }

    public PlacementType getType() {
        return type;
    }

    public enum PlacementType {
        IN_BANNER(2),
        IN_ARTICLE(3),
        IN_FEED(4);

        private final int value;

        PlacementType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
