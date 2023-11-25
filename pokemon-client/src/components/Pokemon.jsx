export default function Pokemon({ stats }) {

  if (stats == null) return null;

  return (
    <div className="enemy pokemon pokemon--align-end">
      <img className="pokemon__image" src={stats.image}/>
      <div>
        <div className="pokemon__status">
          <div className="status__name">{stats.name} Lv: {stats.lvl}</div>
          <div className="status__health-bar">
            <div className="health-bar__backdrop">
              <div
                className="health-bar__current-health"
                style={{width: `${stats.hpCurrent / stats.maxHp * 100}%`}}
              ></div>
            </div>
          </div>
          <div className="enemyHp status__health">
            <span className="currentHealth">{stats.hpCurrent}</span>/
            <span className="maxHealth">{stats.maxHp}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
