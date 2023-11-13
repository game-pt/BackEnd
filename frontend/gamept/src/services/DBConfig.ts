export const DBConfig = {
  name: 'PromptData',
  version: 1,
  objectStoresMeta: [
    {
      store: 'prompt',
      storeConfig: { keyPath: 'id', autoIncrement: false },
      storeSchema: [
        { name: 'content', keypath: 'content', options: { unique: true } },
      ],
    },
  ],
};
