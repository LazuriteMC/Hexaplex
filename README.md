# Hexaplex

![](https://github.com/LazuriteMC/Hexaplex/blob/main/src/main/resources/assets/hexaplex/icon.png?raw=true)

[![GitHub](https://img.shields.io/github/license/LazuriteMC/Hexaplex?color=A31F34&label=License&labelColor=8A8B8C)](https://github.com/LazuriteMC/Hexaplex/blob/main/LICENSE)
[![Discord](https://img.shields.io/discord/719662192601071747?color=7289DA&label=Discord&labelColor=2C2F33&logo=Discord)](https://discord.gg/NNPPHN7b3P)

---

## What is Hexaplex?

Hexaplex is a colorblindness correction mod for Minecraft written for the Fabric Mod Loader.

## What isn't Hexaplex?

Hexaplex *isn't* a shader pack or a resource pack. This means that you can run this mod alongside your favorite 
shaders and resources without conflict.

Note: Optifabric is currently incompatible. This will be resolved by a 1.0.0 release.

## How does it work?

Hexaplex utilizes a process called Daltonization which has the following four steps.

* Convert the original color to one that simulates what a person with a color vision deficiency would perceive.
* Calculate the difference between the original color and the simulated one.
* Shift the difference towards the visible spectrum of the person's color vision deficiency.
* Correct the original color by adding the corrected difference to it.

On the technical side, Hexaplex uses OpenGL shaders and applies Daltonization as a post-processing filter.

## Examples

![](https://github.com/LazuriteMC/lazuritemc.github.io/blob/master/img/hexaplex_examples.png)

Clockwise from top-left: Normal, Protanomaly, Tritanomaly, Deuteranomaly. All examples images were taken with 50% strength and 50% skew.

## What does "Hexaplex" mean?

[Hexaplex](https://en.wikipedia.org/wiki/Hexaplex) is a genus of sea snail that was used by the Minoans, 
Canaanites/Phoenicians, Hebrews, and classical Greeks in the production of rare blue and purple dyes.

## How to install

Hexaplex can be downloaded from [Modrinth](https://modrinth.com/mod/hexaplex), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/hexaplex), or here on [GitHub](https://github.com/LazuriteMC/Hexaplex/releases). It depends on [Mod Menu](https://github.com/TerraformersMC/ModMenu) in order to configure the settings as it defaults to not applying a filter.

## Special Thanks

Hexaplex wouldn't be possible without the following mods:

* [Ladysnake/Satin](https://github.com/Ladysnake/Satin)
* [FabLabsMc/Fiber](https://github.com/FabLabsMC/fiber)

## Issues and Suggestions

Please check out our [Discord Server](https://discord.gg/efCMR7U) if you are experiencing issues or would like to 
make a suggestion. We are always taking feedback on our mods and would like to hear yours.
