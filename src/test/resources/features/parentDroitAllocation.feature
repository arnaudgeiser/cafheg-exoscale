Feature: Allocations

  Quel parent à le droit aux allocations ?

  Scenario: Aucun des parents a une activité lucrative
    Given Aucun parent n'exerce une activité lucrative
    When Je demande à qui revient le droit aux allocations
    Then Aucun des parents n'a le droit aux allocations

  Scenario Outline: Un seul parent avec activité lucrative
    Given Un seul parent a une activité lucrative "<Parent1>" "<Parent2>"
    When Je demande à qui revient le droit aux allocations
    Then Le parent qui exerce une activité lucrative a le droit aux allocations "<expected>"

    Examples:
      | Parent1 | Parent2 | expected |
      | true    | false   | Parent1  |
      | false   | true    | Parent2  |

  Scenario Outline: Un seul parent avec l'autorité parentale
    Given Les deux parents ont une activité lucrative
    And Un seul parent a l'autorité parentale "<Parent1>" "<Parent2>"
    When Je demande à qui revient le droit aux allocations
    Then Le parent qui a l'autorité parentale a le droit aux allocations "<expected>"

    Examples:
      | Parent1 | Parent2 | expected |
      | true    | false   | Parent1  |
      | false   | true    | Parent2  |

  Scenario Outline: Un seul parent vit avec l'enfant
    Given Les deux parents ont une activité lucrative
    And Les deux parents ont l'autorité parentale
    And Les parents vivent séparés "<Parent1>" "<Parent2>"
    When Je demande à qui revient le droit aux allocations
    Then Le parent qui vit avec l'enfant a le droit aux allocations "<expected>"

    Examples:
      | Parent1  | Parent2  | expected |
      | Fribourg | Bienne   | Parent1  |
      | Bienne   | Fribourg | Parent2  |

  Scenario Outline: Un seul parent travaille dans le canton de domicile de l'enfant
    Given Les deux parents ont une activité lucrative
    And Les deux parents ont l'autorité parentale
    And Les parents vivent ensemble
    And Un seul parent travaille dans le canton de domicile de l'enfant "<Parent1>" "<Parent2>"
    When Je demande à qui revient le droit aux allocations
    Then Le parent qui travaille dans le canton de domicile de l'enfant a le droit aux allocations "<expected>"

    Examples:
      | Parent1 | Parent2 | expected |
      | FR      | BE      | Parent1  |
      | BE      | FR      | Parent2  |

  Scenario Outline: Un parent est salarié et l'autre parent est indépendant
    Given Les deux parents ont une activité lucrative
    And Les deux parents ont l'autorité parentale
    And Les parents vivent ensemble
    And Les parents travaillent dans le canton de domicile de l'enfant
    And Un parent est salarié et l'autre parent est indépendant "<Parent1Sal>" "<Parent1Ind>" "<Parent2Sal>" "<Parent2Ind>"
    When Je demande à qui revient le droit aux allocations
    Then Le parent qui est salarié a le droit aux allocations "<expected>"

    Examples:
      | Parent1Sal | Parent1Ind | Parent2Sal | Parent2Ind | expected |
      | true       | false      | false      | true       | Parent1  |
      | false      | true       | true       | false      | Parent2  |

  Scenario Outline: Les deux parents sont salariés
    Given Les deux parents ont une activité lucrative
    And Les deux parents ont l'autorité parentale
    And Les parents vivent ensemble
    And Les parents travaillent dans le canton de domicile de l'enfant
    And Les deux parents sont salariés "<Parent1>" "<Parent2>"
    When Je demande à qui revient le droit aux allocations
    Then Le parent avec le revenu soumis à l'AVS le plus élevé a le droit aux allocations "<expected>"

    Examples:
      | Parent1 | Parent2 | expected |
      | 5000    | 4000    | Parent1  |
      | 4000    | 5000    | Parent2  |

  Scenario Outline: Les deux parents sont indépendants
    Given Les deux parents ont une activité lucrative
    And Les deux parents ont l'autorité parentale
    And Les parents vivent ensemble
    And Les parents travaillent dans le canton de domicile de l'enfant
    And Les deux parents sont indépendants "<Parent1>" "<Parent2>"
    When Je demande à qui revient le droit aux allocations
    Then Le parent avec le revenu soumis à l'AVS le plus élevé a le droit aux allocations "<expected>"

    Examples:
      | Parent1 | Parent2 | expected |
      | 5000    | 4000    | Parent1  |
      | 4000    | 5000    | Parent2  |