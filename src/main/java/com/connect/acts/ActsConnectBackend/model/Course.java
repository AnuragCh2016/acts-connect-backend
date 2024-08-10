package com.connect.acts.ActsConnectBackend.model;

import java.util.Arrays;
import java.util.List;

public enum Course {
    DAC(1, "Diploma in Advanced Computing", "DAC-101", 60, 2, Arrays.asList()),
    DBDA(2, "Diploma in Big Data Analytics", "DBDA-1001", 85, 3, Arrays.asList(DAC)),
    DESD(3, "Diploma in Embedded Systems Design", "DESD-1001", 75, 2, Arrays.asList(DAC)),
    DMC(4, "Diploma in Mobile Computing", "DMC-1001", 70, 2, Arrays.asList(DAC)),
    DVLSI(5, "Diploma in VLSI Design", "DVLSI-1001", 65, 2, Arrays.asList(DAC)),
    DIOT(6, "Diploma in Internet of Things", "DIOT-1001", 70, 2, Arrays.asList(DAC)),
    DRAT(7, "Diploma in Robotics and Autonomous Systems", "DRAT-1001", 80, 2, Arrays.asList(DAC, DESD)),
    DUASP(8, "Diploma in Unix and Shell Programming", "DUASP-1001", 60, 2, Arrays.asList()),
    DAI(9, "Diploma in Artificial Intelligence", "DAI-1001", 90, 3, Arrays.asList(DAC, DBDA)),
    DHPCAP(10, "Diploma in High Performance Computing and Artificial Intelligence", "DHPCAP-1001", 95, 3, Arrays.asList(DAC)),
    DHPCSA(11, "Diploma in HPC System Administration", "DHPCSA-1001", 90, 3, Arrays.asList(DAC, DBDA, DESD)),
    DITISS(12, "Diploma in IT Infrastructure, Systems, and Security", "DITISS-1001", 85, 3, Arrays.asList(DAC)),
    DCSF(13, "Diploma in Cyber Security and Forensics", "DCSF-1001", 80, 3, Arrays.asList(DAC)),
    DFBD(14, "Diploma in FinTech and Blockchain Development", "DFBD-1001", 70, 2, Arrays.asList(DAC)),
    DASSD(15, "Diploma in Advanced Secure Software Development", "DASSD-1001", 85, 3, Arrays.asList(DAC)),
    CLOUDSEC(16, "Diploma in Cloud Services and Security", "CLOUDSEC-1001", 80, 3, Arrays.asList(DAC, DIOT)),
    DUASP_2(17, "Diploma in Unmanned Aircraft System Programming", "DUASP-1002", 75, 2, Arrays.asList(DAC)); // Renamed to DUASP_2 to avoid duplicate

    private final int id;
    private final String description;
    private final String code;
    private final int credits;
    private final int duration;
    private final List<Course> prerequisites;

    Course(int id, String description, String code, int credits, int duration, List<Course> prerequisites) {
        this.id = id;
        this.description = description;
        this.code = code;
        this.credits = credits;
        this.duration = duration;
        this.prerequisites = prerequisites;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public int getCredits() {
        return credits;
    }

    public int getDuration() {
        return duration;
    }

    public List<Course> getPrerequisites() {
        return prerequisites;
    }
}
