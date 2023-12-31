/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package me.modmuss50.voyager.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.structure.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;

/**
 * This fixes a rare CME in the StructureManager when using Java 11 or newer.
 *
 * <p>See: <a href="https://bugs.mojang.com/browse/MC-149777">MC-149777</a>
 */
@Mixin(StructureManager.class)
public abstract class StructureManagerMixin {
    @Coerce
    @ModifyExpressionValue(method = {
            /* 20w17a-21w19a */ "<init>(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lcom/mojang/datafixers/DataFixer;)V",
            /* 18w43a-20w16a */ "Lnet/minecraft/structure/StructureManager;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/io/File;Lcom/mojang/datafixers/DataFixer;)V"
    }, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Maps;newHashMap()Ljava/util/HashMap;"))
    private Map<Identifier, Structure> init(HashMap<Identifier, Structure> original) {
        return Collections.synchronizedMap(original);
    }
}
