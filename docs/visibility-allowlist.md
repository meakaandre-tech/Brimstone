# What Quicksilver's visibility module may hide (package 2)

Quicksilver hides an entity or block entity only when its occlusion test proves that a box around it can't be seen
from the camera (`src/main/java/com/meakaandre/quicksilver/visibility/Occlusion.java`). That is only correct if the box
contains everything the object's renderer can draw. So only the types below are ever tested (`Visibility.ENTITY_MARGIN`,
`Visibility.BLOCK_ENTITY_TYPES`); every other type, vanilla or modded, is drawn as vanilla decides. The object's own
class and its renderer's class must also be vanilla's (`net.minecraft.`): a mod that replaces the renderer of a listed
type takes that type off the list.

Source of the extents: vanilla's models and renderers as they are in 26.3. The model classes' bytecode is not in CI's
dump, so the figures below come from the vanilla model layouts, in model pixels (16 per block) times the renderer's
scale. Each margin was chosen with at least 0.5 blocks to spare over the largest reach found. Where a reach depends on
an animation, the extreme pose is used.

## Entities: vanilla culling box + 2 blocks on every side (times the entity's scale attribute, at least 1)

The box is vanilla's own culling box (`EntityRenderer.getBoundingBoxForCulling`, the interpolated bounding box, so it
follows movement within the frame), grown by 2 blocks on every side, times `LivingEntity.getScale()` when that is above
1. Model, bounding box and shadow all scale together.

The same for every type:
- **Shadow:** radius at most 1.0 block (spider; most are 0.3-0.7). It lies within that radius of the entity's position
  and at most one radius below its feet, so it is inside the 2-block margin.
- **Fire overlay:** drawn on quads about 1.4 times the box width and as high as the box, inside the margin.
- **Excluded per entity** (never hidden whatever the type):
  - a custom name: name tags draw through walls;
  - glowing or outlined (`isCurrentlyGlowing`, `Minecraft.shouldEntityAppearGlowing`);
  - leashed: the lead is drawn by the leashed entity and reaches to its holder;
  - riding or ridden: passengers are drawn at their own positions, and the frustum rules for them are vanilla's;
  - the camera entity;
  - sleeping: the lying model reaches along the floor, outside the sleeping box;
  - dying: the death animation tips the model over by its height;
  - not culled by vanilla either (`affectedByCulling`).

| type | box (w x h) | largest reach outside the box | margin |
|---|---|---|---|
| cow, mooshroom | 0.9 x 1.4 | head and horns about 0.5 forward of the box; mooshroom's mushrooms about 0.4 above the back | 2 |
| pig | 0.9 x 0.9 | snout about 0.3 forward; saddle on the body | 2 |
| sheep | 0.9 x 1.3 | head lowered while grazing, about 0.5 forward and down | 2 |
| chicken | 0.4 x 0.7 | wings flapping, about 0.3 to the side; beak and wattle about 0.2 forward | 2 |
| villager | 0.6 x 1.95 | nose and crossed arms about 0.4 forward; the item it holds out while trading about 0.6 forward; profession hats about 0.2 above the head | 2 |
| iron golem | 1.4 x 2.7 | arms about 1.9 long from the shoulder. The attack swing raises them forward: tips at most about 1.3 outside the box side. The offered poppy is held in the hand. | 2 |
| snow golem | 0.7 x 1.9 | stick arms about 0.6 to the side; pumpkin head inside the box height | 2 |
| zombie | 0.6 x 1.95 | arms held forward about 0.75, plus a held item or tool about 0.6: at most about 1.3 forward | 2 |
| skeleton | 0.6 x 1.99 | arms and a drawn bow: at most about 1.0 forward | 2 |
| creeper | 0.6 x 1.7 | swells by up to 1.2 times before exploding; the charged aura is a slightly larger copy of the model | 2 |
| spider | 1.4 x 0.9 | legs reach about 1.25 from the centre, at most about 0.6 outside the box | 2 |
| item | 0.25 x 0.25 | the item model at item-entity scale, bobbing and spinning; large items reach at most about 0.5 | 2 |

**Not on the list, with the reason** (examples, not exhaustive; anything not listed above is never hidden):
- **Ghast, happy ghast, ghastling:** tentacles hang 2.4-4.1 blocks below the feet at the renderer's 4.5 scale (the
  review's figures); the occlusion scene shows a happy ghast whose tentacles hang in the window.
- **Squid, glow squid:** tentacles about 1.6 blocks from the centre, too close to a margin to trust without
  measuring.
- **Ender dragon:** a multi-part entity.
- **Guardian, elder guardian:** beams to their target.
- **Frog:** its tongue reaches the target.
- **Phantom, horse family, camel, sniffer, ravager, warden, wither, others:** large or long models not checked.
- **Players and mannequins:** name tags, capes and cosmetics from other mods.
- **Display entities:** arbitrary transforms.
- **Item frames, paintings, armour stands, minecarts, boats, projectiles:** not checked one by one, so not hidden.

## Block entities: the block grown by 1 block on every side

Only types whose renderer draws inside that box, in every state:

| type | what the renderer draws, and how far it reaches |
|---|---|
| chest, trapped chest, ender chest | the chest and its lid. The open lid stands about 0.9 above the top. A double chest draws its other half in the neighbouring block. |
| sign, hanging sign | the board, the post or chains, and the text, all inside the block |
| banner | standing banner: pole and cloth up to about 1.9 above the block's base. Wall banner: cloth hanging up to about 1 below the block. |
| bell | the bell and its swing, inside the block |
| shulker box | the shell, and the lid opening upward and turning, about 0.5 above the top |
| decorated pot | the pot and its wobble, inside the block |
| campfire | up to four items lying on the fire, inside the block |
| lectern | the book on the lectern, inside the block |
| enchanting table | the floating book, about 0.25 above the top |

**Not on the list:**
- **End gateway:** draws a beam hundreds of blocks long, and its renderer doesn't override `shouldRenderOffScreen`
  (the review's blocker).
- **Beacon:** beam.
- **Spawner, trial spawner, vault:** a spinning entity or item of any size.
- **Conduit:** animated parts around the block.
- **End portal:** a portal face.
- **Moving pistons:** a moved block at an offset.
- **Skulls, beds** (not block entities in 26.3), **brushable blocks, shelves, copper golem statues, structure blocks,
  test blocks, any modded block entity:** not checked.

## Occluders

Only full opaque cubes of the `minecraft` namespace (`BlockState.isSolidRender()`, a plain model, no random offset),
only with their face toward the camera drawn, and only in sections that vanilla draws this frame, are fully faded in,
and whose mesh is newer than the last change there. If any pack other than the game's own supplies a block texture,
block model or blockstate in the `minecraft` namespace, the module hides nothing at all (`VisibilityResources`; log line
`Quicksilver visibility: deferring to vanilla ...`). In 26.3 a quad's draw layer follows its texture's transparency, so
such a pack could make an "opaque" block see-through.
