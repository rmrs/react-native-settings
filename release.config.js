module.exports = {
  plugins: [
    [
      '@semantic-release/git',
      {
        assets: ['package.json'],
        message:
          'chore(release): ${nextRelease.version} [skip ci]\n\n${nextRelease.notes}',
      },
    ],
  ],
};
