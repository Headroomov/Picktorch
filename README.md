# Picktorch

Picktorch is a client-side Fabric mod that lets you place torches from your inventory while holding a pickaxe.

Right-click a block with a pickaxe in your main hand, and Picktorch will try to use a torch from your inventory without making you switch hotbar slots manually.

---

## Features

- Place torches while holding a pickaxe
- Use torches directly from your inventory
- Keep your selected hotbar slot unchanged
- Works in singleplayer and on multiplayer servers
- Does not require installation on the server
- Uses normal Minecraft torch placement behavior

Supported items:

- Torch
- Soul Torch

---

## How it works

Picktorch does not place blocks directly and does not modify the world on its own.

When you right-click a block with a pickaxe, the mod temporarily stages a torch in your main hand, lets Minecraft handle the block interaction normally, and then restores your original held item.

This means placement is still handled by Minecraft and the server. Block placement rules, protected areas, game mode restrictions, and anti-cheat checks continue to apply.

---

## Server compatibility

Picktorch is designed to behave like a normal player action. It does not grant extra permissions and cannot bypass server-side restrictions.

If a server would reject a normal torch placement, Picktorch will not force it.

Some public servers may still restrict client-side automation or quality-of-life mods. Please check the server rules before using Picktorch on multiplayer servers.

---

## Requirements

- Fabric Loader
- Fabric API
- Java 21 or newer

---

## License

MIT