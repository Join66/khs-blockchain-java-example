package simple.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleBlockchain<T extends Tx> {
	public static final int BLOCK_SIZE = 10;
	public List<Block<T>> chain = new ArrayList<Block<T>>();

	public SimpleBlockchain() {
		// create genesis block
		chain.add(newBlock());
	}

	public SimpleBlockchain(List<Block<T>> blocks) {
		this();
		chain = blocks;
	}

	public Block<T> getHead() {

		Block<T> result = null;
		if (this.chain.size() > 0) {
			result = this.chain.get(this.chain.size() - 1);
		} else {

			throw new RuntimeException("No Block's have been added to chain...");
		}

		return result;
	}

	public void addAndValidateBlock(Block<T> block) {

		// compare previous block hash back to genesis hash
		Block<T> current = block;
		for (int i = chain.size() - 1; i >= 0; i--) {
			Block<T> b = chain.get(i);
			if (b.getHash().equals(current.getPreviousHash())) {
				current = b;
			} else {

				throw new RuntimeException("Block Invalid");
			}

		}

		this.chain.add(block);

	}

	public boolean validate() {

		String previousHash = chain.get(0).getHash();
		for (Block<T> block : chain) {
			String currentHash = block.getHash();
			if (!currentHash.equals(previousHash)) {
				return false;
			}

			previousHash = currentHash;

		}

		return true;

	}

	public Block<T> newBlock() {
		int count = chain.size();
		String previousHash = "root";

		if (count > 0)
			previousHash = blockChainHash(); // 当前区块的上一个区块hash

		Block<T> block = new Block<T>();

		block.setTimeStamp(System.currentTimeMillis());
		block.setIndex(count);
		block.setPreviousHash(previousHash);
		return block;
	}

	public SimpleBlockchain<T> add(T item) {

		if (chain.size() == 0) {
			// genesis block
			this.chain.add(newBlock()); // 区块为0 则第一个区块需要为创世区块
		}

		// See if head block is full
		if (getHead().getTransactions().size() >= BLOCK_SIZE) { // head即最新区块（不包括还未生成的区块）
			this.chain.add(newBlock()); // 交易数超过"BLOCK_SIZE",生成新的区块
		}

		getHead().add(item); // 如果交易数超过"BLOCK_SIZE"，则将新的交易放入新的区块中

		return this;
	}

	/* Deletes the index of the after. */
	public void DeleteAfterIndex(int index) {
		if (index >= 0) {
			Predicate<Block<T>> predicate = b -> chain.indexOf(b) >= index;
			chain.removeIf(predicate);
		}
	}

	public SimpleBlockchain<T> Clone() {
		List<Block<T>> clonedChain = new ArrayList<Block<T>>();
		Consumer<Block> consumer = (b) -> clonedChain.add(b.Clone());
		chain.forEach(consumer); // 防止调用者修改后，被克隆者跟着修改
		return new SimpleBlockchain<T>(clonedChain);
	}

	public List<Block<T>> getChain() {
		return chain;
	}

	public void setChain(List<Block<T>> chain) {
		this.chain = chain;
	}

	/* Gets the root hash. */
	public String blockChainHash() {
		return getHead().getHash();
	}

}