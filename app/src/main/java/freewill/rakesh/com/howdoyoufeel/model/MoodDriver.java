package freewill.rakesh.com.howdoyoufeel.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(generateGettersSetters = true)
public class MoodDriver {

    @Id
    private Long moodDriverId;
    @Property(nameInDb = "asc_sign")@NotNull
    int ascendantSign;
    @Property(nameInDb = "sun_pos")@NotNull
    int sunPosition;
    @Property(nameInDb = "mon_pos")@NotNull
    int moonPosition;
    @Property(nameInDb = "rah_pos")@NotNull
    int rahuPosition;
    @Property(nameInDb = "ket_pos")@NotNull
    int ketuPosition;
    @Property(nameInDb = "mar_pos")@NotNull
    int marPosition;
    @Property(nameInDb = "sat_pos")@NotNull
    int saturnPosition;
    @Property(nameInDb = "jup_pos")@NotNull
    int jupiterPosition;
    @Property(nameInDb = "mer_pos")@NotNull
    int mercuryPosition;
    @Property(nameInDb = "ven_pos")@NotNull
    int venusPosition;

    private Long moodId;

    @ToOne(joinProperty= "moodId")
    private Mood mood;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1093896270)
    private transient MoodDriverDao myDao;

    @Generated(hash = 886848837)
    public MoodDriver(Long moodDriverId, int ascendantSign, int sunPosition,
            int moonPosition, int rahuPosition, int ketuPosition, int marPosition,
            int saturnPosition, int jupiterPosition, int mercuryPosition,
            int venusPosition, Long moodId) {
        this.moodDriverId = moodDriverId;
        this.ascendantSign = ascendantSign;
        this.sunPosition = sunPosition;
        this.moonPosition = moonPosition;
        this.rahuPosition = rahuPosition;
        this.ketuPosition = ketuPosition;
        this.marPosition = marPosition;
        this.saturnPosition = saturnPosition;
        this.jupiterPosition = jupiterPosition;
        this.mercuryPosition = mercuryPosition;
        this.venusPosition = venusPosition;
        this.moodId = moodId;
    }

    @Generated(hash = 1081355898)
    public MoodDriver() {
    }

    public MoodDriver(Mood mood) {
        this.mood = mood;
        this.moodId = mood.getMoodId();
    }

    public Long getMoodDriverId() {
        return this.moodDriverId;
    }

    public void setMoodDriverId(Long moodDriverId) {
        this.moodDriverId = moodDriverId;
    }

    public int getAscendantSign() {
        return this.ascendantSign;
    }

    public void setAscendantSign(int ascendantSign) {
        this.ascendantSign = ascendantSign;
    }

    public int getSunPosition() {
        return this.sunPosition;
    }

    public void setSunPosition(int sunPosition) {
        this.sunPosition = sunPosition;
    }

    public int getMoonPosition() {
        return this.moonPosition;
    }

    public void setMoonPosition(int moonPosition) {
        this.moonPosition = moonPosition;
    }

    public int getRahuPosition() {
        return this.rahuPosition;
    }

    public void setRahuPosition(int rahuPosition) {
        this.rahuPosition = rahuPosition;
    }

    public int getKetuPosition() {
        return this.ketuPosition;
    }

    public void setKetuPosition(int ketuPosition) {
        this.ketuPosition = ketuPosition;
    }

    public int getMarPosition() {
        return this.marPosition;
    }

    public void setMarPosition(int marPosition) {
        this.marPosition = marPosition;
    }

    public int getSaturnPosition() {
        return this.saturnPosition;
    }

    public void setSaturnPosition(int saturnPosition) {
        this.saturnPosition = saturnPosition;
    }

    public int getJupiterPosition() {
        return this.jupiterPosition;
    }

    public void setJupiterPosition(int jupiterPosition) {
        this.jupiterPosition = jupiterPosition;
    }

    public int getMercuryPosition() {
        return this.mercuryPosition;
    }

    public void setMercuryPosition(int mercuryPosition) {
        this.mercuryPosition = mercuryPosition;
    }

    public int getVenusPosition() {
        return this.venusPosition;
    }

    public void setVenusPosition(int venusPosition) {
        this.venusPosition = venusPosition;
    }

    public Long getMoodId() {
        return this.moodId;
    }

    public void setMoodId(Long moodId) {
        this.moodId = moodId;
    }

    @Generated(hash = 1683375380)
    private transient Long mood__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 28364958)
    public Mood getMood() {
        Long __key = this.moodId;
        if (mood__resolvedKey == null || !mood__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MoodDao targetDao = daoSession.getMoodDao();
            Mood moodNew = targetDao.load(__key);
            synchronized (this) {
                mood = moodNew;
                mood__resolvedKey = __key;
            }
        }
        return mood;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 825441302)
    public void setMood(Mood mood) {
        synchronized (this) {
            this.mood = mood;
            moodId = mood == null ? null : mood.getMoodId();
            mood__resolvedKey = moodId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 435266069)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMoodDriverDao() : null;
    }
}
