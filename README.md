# Hexaplex

![](https://github.com/LazuriteMC/Hexaplex/blob/main/src/main/resources/assets/rayon/icon.png?raw=true)

[![GitHub](https://img.shields.io/github/license/LazuriteMC/Hexaplex?color=A31F34&label=License&labelColor=8A8B8C)](https://github.com/LazuriteMC/Hexaplex/blob/main/LICENSE)
[![Discord](https://img.shields.io/discord/719662192601071747?color=7289DA&label=Discord&labelColor=2C2F33&logo=Discord)](https://discord.gg/efCMR7U)

---

## What is Hexaplex?

Hexaplex is a colorblindness correction mod for Minecraft written for the Fabric Mod Loader.

## What isn't Hexaplex?

Hexaplex *isn't* a shader pack or a resource pack. This means that you can run this mod alongside your favorite 
shaders and resources without conflict.

Note: Optifabric is currently incompatible. This will be resolved by a 1.0.0 release.

## How does it work?

Hexaplex utilizes a process called Daltonization which has the following four steps.

* Convert the original image to one that simulates what a person with a color vision deficiency would perceive.
* Calculate the difference between the original image and the simulated one.
* Shift the color differences towards the visible spectrum of the user's vision deficiency.
* Correct the original image by adding the corrected differences to it.

On the technical side, Hexaplex uses OpenGL shaders and applies Daltonization as a post-processing filter.

## What does "Hexaplex" mean?

[Hexaplex](https://en.wikipedia.org/wiki/Hexaplex) is a genus of sea snail that was used by the Minoans, 
Canaanites/Phoenicians, Hebrews, and classical Greeks in the production of rare blue and purple dyes.

## Special Thanks

Hexaplex wouldn't be possible without the following mods:

* [Ladysnake/Satin](https://github.com/Ladysnake/Satin)
* [FabLabsMc/Fiber](https://github.com/FabLabsMC/fiber)

## Issues and Suggestions

Please check out our [Discord Server](https://discord.gg/efCMR7U) if you are experiencing issues or would like to 
make a suggestion. We are always taking feedback on our mods and would like to hear yours.
