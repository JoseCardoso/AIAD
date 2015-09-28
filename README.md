##AIAD

##Membros do Grupo:

- [Diogo Soares] (https://github.com/diogoei12102)
- [Gabriel Souto](https://github.com/Inframan)
- [José Cardoso](https://github.com/JoseCardoso)

#Coordenação inteligente de semáforos

##Objetivo
Explorar diferentes estratégias de coordenação de semáforos usando um Sistema MultiAgente.

##Descrição
O objetivo principal deste trabalho é testar estratégias de coordenação semafórica, a partir da
configuração inteligente dos parâmetros associados à temporização das luzes, utilizando a
metáfora dos agentes inteligentes.

A partir de sensores, os agentes semáforos devem ter acesso ao volume de tráfego chegando à
sua área de intervenção. Esta informação será utilizada para que o agente selecione o melhor
plano semafórico a adotar. Diferentes estratégias devem ser testadas, nomeadamente:
  - a possibilidade de comunicação entre vários semáforos vizinhos, quer com o objetivo
de enriquecer a circulação de informação entre os agentes, quer com vista a uma
coordenação multi-agente mais eficaz;

  -a exploração de estratégias baseadas em mercado (ver artigo abaixo).
  
A análise da qualidade de cada estratégia poderá ser realizada através de medidas de
desempenho tais como o tempo de espera nas filas, a velocidade média na rede, ou a razão
entre veículos entrados e saídos.
O trabalho inclui a construção de um modelo de simulação de tráfego ou a utilização do SUMO
(Simulation of Urban MObility).

##Material
- JADE, Repast+SAJaS, SUMO+TraSMAPI
- Raphael, J., S. Maskell, and E. Sklar, From Goods to Traffic: First Steps Toward an
Auction-Based Traffic Signal Controller, in Advances in Practical Applications of
Agents, Multi-Agent Systems, and Sustainability: The PAAMS Collection, Y.
Demazeau, et al., Editors. 2015, Springer International Publishing. p. 187-198
- Tiago M. L. Azevedo, Paulo J. M. De Araújo, Rosaldo J. F. Rossetti, Ana Paula C.
Rocha. "JADE, TraSMAPI and SUMO: A tool-chain for simulating traffic light control".
ATT 2014, 8th International Workshop on Agents in Traffic and Transportation, held
at AAMAS 2014, May 5-6, Paris, France. pp. 8-15.
