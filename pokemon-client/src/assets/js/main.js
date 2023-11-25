function setStartingHealth(target, hp) {
  const currentHealthEl = document.querySelector(`.${target} .currentHealth`);
  const maxHealthEl = document.querySelector(`.${target} .maxHealth`);

  currentHealthEl.innerHTML = maxHealthEl.innerHTML = hp;
}

setStartingHealth("player", 30);
setStartingHealth("enemy", 28);

