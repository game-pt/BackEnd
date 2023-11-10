export const DBConfig = {
  name: 'PromptData',
  version: 1,
  objectStoresMeta: [
    {
      store: 'prompt',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'content', keypath: 'content', options: { unique: false } },
      ],
    },
    {
      store: 'codeStore',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'gameCode', keypath: 'gameCode', options: { unique: true } },
        {
          name: 'playerCode',
          keypath: 'playerCode',
          options: { unique: true },
        },
      ],
    },
  ],
};
