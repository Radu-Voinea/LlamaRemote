package com.crazyllama.llama_remote.server.dto;

import com.crazyllama.llama_remote.server.manager.DatabaseManager;
import com.raduvoinea.utils.logger.Logger;

public interface IDatabaseEntry<Identifier> {

	Identifier getIdentifier();

	default void save() {
		Logger.debug("Saving " + this.getClass().getSimpleName() + " with identifier " + getIdentifier());
		DatabaseManager.instance().getSessionFactory().inTransaction(transaction -> {
			Object existingEntity = transaction.get(this.getClass(), getIdentifier());
			if (existingEntity == null) {
				Logger.debug("Persisting " + this.getClass().getSimpleName() + " with identifier " + getIdentifier());
				transaction.persist(this);
			} else {
				Logger.debug("Merging " + this.getClass().getSimpleName() + " with identifier " + getIdentifier());
				transaction.merge(this);
			}
		});
	}

	default void delete() {
		DatabaseManager.instance().getSessionFactory().inTransaction(transaction -> transaction.remove(this));
	}

}
