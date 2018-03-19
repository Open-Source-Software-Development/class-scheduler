package osd.database;

import osd.input.Block;

import javax.inject.Inject;

public class BlockFactory implements DatabaseFactory<BlockRecord, Block> {

	@Inject
    BlockFactory(/* Add arguments here, if needed.*/) {
		// Do any setup you need here.
	}

	@Override
	public Block create(final BlockRecord record) {
		// TODO: implement this!
		throw new UnsupportedOperationException("not yet implemented");
	}

}
