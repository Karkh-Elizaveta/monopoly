package game.core.structs;

public enum StreetType {
    RED {
        public Integer getAmount() {
            return 3;
        }
    },
    YELLOW {
        public Integer getAmount() {
            return 3;
        }
    },
    ORANGE {
        public Integer getAmount() {
            return 3;
        }
    },
    GREEN {
        public Integer getAmount() {
            return 3;
        }
    },
    LIGHT_BLUE {
        public Integer getAmount() {
            return 3;
        }
    },
    BLUE {
        public Integer getAmount() {
            return 2;
        }
    },
    PURPLE {
        public Integer getAmount() {
            return 3;
        }
    },
    PINK {
        public Integer getAmount() {
            return 2;
        }
    };

    public abstract Integer getAmount();
}
