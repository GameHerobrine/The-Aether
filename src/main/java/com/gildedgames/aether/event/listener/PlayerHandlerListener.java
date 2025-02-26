package com.gildedgames.aether.event.listener;

import com.gildedgames.aether.player.AetherPlayerHandler;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;

public class PlayerHandlerListener {
	@EventListener
    public void registerPlayerHandlers(PlayerEvent.HandlerRegister event) {
		event.playerHandlers.add(new AetherPlayerHandler(event.player));
	}
}
