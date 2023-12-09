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

import net.minecraft.structure.*;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

/**
 * This fixes a rare CME in the StructureManager when using Java 11 or newer.
 *
 * <p>See: <a href="https://bugs.mojang.com/browse/MC-149777">MC-149777</a>
 */
@Mixin(StructureManager.class)
public abstract class StructureManagerMixin {
    @Mutable
    @Shadow
    @Final
    private Map<Identifier, Structure> structures;

    @Inject(method = "<init>", at = @At(value = "FIELD",
            target = "Lnet/minecraft/structure/StructureManager;structures:Ljava/util/Map;",
            opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER
    ))
    private void init(CallbackInfo ci) {
        this.structures = Collections.synchronizedMap(this.structures);
    }
}
