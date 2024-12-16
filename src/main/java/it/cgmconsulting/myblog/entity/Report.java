package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.enumeration.ReportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Report extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private MadeByYou madeByYou;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 18)
    private ReportStatus status;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="reason"),
            @JoinColumn(name="start_date")
    })
    private ReportReason reportReason;

    @ManyToMany
    @JoinTable(name = "reporter_users",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> reporters = new HashSet<>();

}
