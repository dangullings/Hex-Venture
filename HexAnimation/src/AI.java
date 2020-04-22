import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// dont leave self out to dry where I can get behind them

public class AI {
	Hex[] testHexes = new Hex[hexVenture.BSIZE];
	Hex[] testSliceHexes = new Hex[hexVenture.BSIZE];
	Player[] testPlayer = new Player[hexVenture.player.length];
	
	ArrayList<Integer> intBaseLinkTerList = new ArrayList<Integer>();
	ArrayList<Integer> intMoveList = new ArrayList<Integer>();
	ArrayList<Integer> intAllOwnTerList = new ArrayList<Integer>();
	ArrayList<Integer> intOwnTerList = new ArrayList<Integer>();
	ArrayList<Integer> intOwnFighterTerList = new ArrayList<Integer>();
	ArrayList<Integer> intNeutralTerList = new ArrayList<Integer>();
	ArrayList<Integer> intEnemyTerList = new ArrayList<Integer>();
	ArrayList<Integer> intEnemyTerDivList = new ArrayList<Integer>();
	ArrayList<Integer> intEnemyTerDivNeighborList = new ArrayList<Integer>();
	ArrayList<Integer> intEnemyFighterTerList = new ArrayList<Integer>();
	ArrayList<Integer> intEnemyFighterNeighborTerList = new ArrayList<Integer>();
	ArrayList<Integer> intEnemyBaseTerList = new ArrayList<Integer>();
	ArrayList<Integer> intEnemyBaseNeighborTerList = new ArrayList<Integer>();
	ArrayList<Integer> intbonusLinkTerList = new ArrayList<Integer>();
	ArrayList<Integer> intOwnFalseTerList = new ArrayList<Integer>();
	
	Random rand = new Random();
	
	int selectedHex;
	int score;
	int bestScore;
	
	public AI(){
		
	}
	
	Hex[] getSliceHexesArrayDeepCopy(Hex[] array){
		for (int i = 0; i < array.length; i++){
			testSliceHexes[i] = new Hex();
			testSliceHexes[i].setColumn(array[i].getColumn());
			testSliceHexes[i].setRow(array[i].getRow());
			testSliceHexes[i].setLinked(array[i].isLinked());
			testSliceHexes[i].setReal(array[i].isReal());
			testSliceHexes[i].setDefendRank(0);
			testSliceHexes[i].setSumRank(0);
			testSliceHexes[i].setThreatRank(0);
			testSliceHexes[i].setFighterCover(false);
			testSliceHexes[i].occupation = new hexOccupation();
			testSliceHexes[i].occupation.setPlayer(array[i].occupation.getPlayer());
			testSliceHexes[i].occupation.setBase(array[i].occupation.getBase());
			testSliceHexes[i].occupation.setFighter(array[i].occupation.getFighter());
			testSliceHexes[i].occupation.setFighterRank(array[i].occupation.getFighterRank());
			testSliceHexes[i].occupation.setOccupiedBy(array[i].occupation.getOccupiedBy());
			for (int n = 0; n < 6; n++)
				testSliceHexes[i].neighbor[n] = array[i].neighbor[n];
		}
		
		return testHexes;
	}
	
	Hex[] getHexesArrayDeepCopy(Hex[] array){
		for (int i = 0; i < array.length; i++){
			testHexes[i] = new Hex();
			testHexes[i].setColumn(array[i].getColumn());
			testHexes[i].setRow(array[i].getRow());
			testHexes[i].setLinked(array[i].isLinked());
			testHexes[i].setReal(array[i].isReal());
			testHexes[i].setDefendRank(0);
			testHexes[i].setSumRank(0);
			testHexes[i].setThreatRank(0);
			testHexes[i].setFighterCover(false);
			testHexes[i].occupation = new hexOccupation();
			testHexes[i].occupation.setPlayer(array[i].occupation.getPlayer());
			testHexes[i].occupation.setBase(array[i].occupation.getBase());
			testHexes[i].occupation.setFighter(array[i].occupation.getFighter());
			testHexes[i].occupation.setFighterRank(array[i].occupation.getFighterRank());
			testHexes[i].occupation.setOccupiedBy(array[i].occupation.getOccupiedBy());
			for (int n = 0; n < 6; n++)
				testHexes[i].neighbor[n] = array[i].neighbor[n];
		}
		
		return testHexes;
	}
	
	Player[] getPlayerArrayDeepCopy(Player[] array){
		for (int i = 0; i < array.length; i++){
			testPlayer[i] = new Player();
			testPlayer[i].intBaseQueue.clear();
			testPlayer[i].setAI(array[i].isAI());
			testPlayer[i].base.clear();
			for (int b = 0; b < hexVenture.player[i].base.size(); b++){
				Base base = new Base();
				testPlayer[i].base.add(base);
				testPlayer[i].base.get(b).intFighterQueue.clear();
				testPlayer[i].base.get(b).setBalance(array[i].base.get(b).getBalance());
				testPlayer[i].base.get(b).setCol(array[i].base.get(b).getCol());
				testPlayer[i].base.get(b).setExpense(array[i].base.get(b).getExpense());
				testPlayer[i].base.get(b).setHexesLinked(array[i].base.get(b).getHexesLinked());
				testPlayer[i].base.get(b).setHexNum(array[i].base.get(b).getHexNum());
				testPlayer[i].base.get(b).setHighestRankedFighter(array[i].base.get(b).getHighestRankedFighter());
				testPlayer[i].base.get(b).setIncome(array[i].base.get(b).getIncome());
				testPlayer[i].base.get(b).setMoney(array[i].base.get(b).getMoney());
				testPlayer[i].base.get(b).setSavings(array[i].base.get(b).getSavings());
				testPlayer[i].base.get(b).setRank(array[i].base.get(b).getRank());
				testPlayer[i].base.get(b).setRow(array[i].base.get(b).getRow());
				testPlayer[i].base.get(b).fighter.clear();
				for (int f = 0; f < hexVenture.player[i].base.get(b).fighter.size(); f++){
					Fighter fighter = new Fighter();
					testPlayer[i].base.get(b).fighter.add(fighter);
					testPlayer[i].base.get(b).fighter.get(f).setMoved(false);
					testPlayer[i].base.get(b).fighter.get(f).setBaseNum(array[i].base.get(b).fighter.get(f).getBaseNum());
					testPlayer[i].base.get(b).fighter.get(f).setHexNum(array[i].base.get(b).fighter.get(f).getHexNum());
					testPlayer[i].base.get(b).fighter.get(f).setCost(array[i].base.get(b).fighter.get(f).getCost());
					testPlayer[i].base.get(b).fighter.get(f).setRank(array[i].base.get(b).fighter.get(f).getRank());
				}
			}
		}
		
		return testPlayer;
	}
	
	ArrayList<Move> runTests(int p){
		ArrayList<Move> bestList = new ArrayList<Move>();
		ArrayList<Move> moveList = new ArrayList<Move>();
		ArrayList<Move> sliceList = new ArrayList<Move>();
		
		bestScore = 0;
		// set defend grid for fighters moved only so far
		for (int tests = 0; tests < 100; tests++){ // iterate thru ai turn attempts
			getHexesArrayDeepCopy(hexVenture.hexes);
			getPlayerArrayDeepCopy(hexVenture.player);

			score = 0;
			moveList.clear();
			
			clearGridRanks();
			
			
			//int ep, eb;
			
			// determine player and base to focus on
			//
			//ep = getEnemyPlayerFocus(p, 0);
			//eb = getEnemyBaseFocus(p, 0);
			
			//if ((ep > -1) && (eb > -1)){ // if enemy and base were chosen
				//sliceList.clear();
				//sliceList.addAll(getBestSliceMoveList(p, 0, ep, eb)); // get Move List that results in largest slice
			//}
			
			
			do{ // completely reset ranks and base/fighter queues after each fighter move
				setHexThreatRank(p);
				fillBaseQueue(p); // also sets grid defense ratings for alive bases
				
				if (testPlayer[p].intBaseQueue.size() == 0) // no base has moves, end this test
					break;
				
				int curBase = testPlayer[p].intBaseQueue.get(0); // just pick first available base with moves
				
				while ((testPlayer[p].base.get(curBase).getMoney()>=(5*hexVenture.FIGHTER_RANK_ONE_EXPENSE)) && (availableHighlight(p, curBase))){ // buy all fighters possible
					purchaseFighter(p, curBase);
					Move move = new Move(curBase, -1, -1, true, false);
					moveList.add(move);
				}

				fillBaseFighterQueue(p, curBase); // also sets grid defense ratings for moved fighters
				
				//upgradeBase(p, curBase);
				setSumRanks();
				
				// determine fighters to upgrade
				
				int moveScore = 0, bestMoveScore = 0, fighter = 0, ff;
				for (int f = 0; f < testPlayer[p].base.get(curBase).intFighterQueue.size(); f++){
					ff = testPlayer[p].base.get(curBase).intFighterQueue.get(f);
					setHighlight(p, curBase, ff);
					setAiMoveLists(p, curBase, ff);
					
					moveScore = getMoveScore(p, curBase, ff);
					
					if (moveScore > bestMoveScore){
						bestMoveScore = moveScore;
						fighter = ff;
					}
				}
				
				prepareAiFighter(p, curBase, fighter);
				Move move = new Move(curBase, fighter, selectedHex, false, false);
				
				moveList.add(move);
			}while (testPlayer[p].intBaseQueue.size() > 0);
			
			for (int i = 0; i < hexVenture.BSIZE; i++)
				if ((testHexes[i].occupation.getPlayer() == p) && (testHexes[i].occupation.getBase() > -1))
					score++;
			
			if (score > bestScore){
				bestScore = score;
				bestList.clear();
				bestList.addAll(moveList);
			}
		}
		
		return bestList;
	}

	ArrayList<Move> getBestSliceMoveList(int p, int b, int ep, int eb){
		ArrayList<Move> bestSliceList = new ArrayList<Move>();
		ArrayList<Move> moveSliceList = new ArrayList<Move>();
		ArrayList<Integer> tempHighlightedHexes = new ArrayList<Integer>();
		ArrayList<Fighter> tempFighters = new ArrayList<Fighter>();
		ArrayList<Fighter> tempEnemyFighters = new ArrayList<Fighter>();
		
		int tempScore = 0, tempBestScore = 0;
		int lastHexMove = -1;
		
		// figure out if move results in split
		// tally amt of split hexes and save move list resulting in biggest split
		
		for (int sliceTests = 0; sliceTests < 1; sliceTests++){
			getSliceHexesArrayDeepCopy(testHexes);
			moveSliceList.clear();
			tempFighters.clear();
			tempEnemyFighters.clear();

			for (int i = 0; i < testPlayer[p].base.get(b).fighter.size(); i++){ // create arraylist enemy fighters
				Fighter fighter = new Fighter();
				fighter.setRank(testPlayer[p].base.get(b).fighter.get(i).getRank());
				fighter.setHexNum(testPlayer[p].base.get(b).fighter.get(i).getHexNum());
				fighter.setIndex(i);
				tempFighters.add(fighter);
			}
				
			for (int i = 0; i < testPlayer[ep].base.get(eb).fighter.size(); i++){ // create arraylist of ai fighters
				Fighter fighter = new Fighter();
				fighter.setRank(testPlayer[ep].base.get(eb).fighter.get(i).getRank());
				fighter.setHexNum(testPlayer[ep].base.get(eb).fighter.get(i).getHexNum());
				tempEnemyFighters.add(fighter);
			}
			
			boolean firstMove = true; int temp = 0;
			while (tempFighters.size() > 0) { // while fighters have yet to be moved
				for (int rank = 1; rank < 5; rank++){ // move lower ranked first
				for (int f = 0; f < tempFighters.size(); f++){
					if (tempFighters.get(f).getRank() != rank)
						continue;
					
					updateSliceDefendRanks(tempEnemyFighters);
					tempHighlightedHexes.clear();
					
					if (firstMove){
						tempHighlightedHexes.addAll(getHexHighlights(rank, p, b, ep, eb, testSliceHexes)); // create integer arraylist with all enemy base neighboring hexes
						firstMove = false;
					}else
						tempHighlightedHexes.addAll(getSelectedHexHighlights(rank, p, b, ep, eb, testSliceHexes, lastHexMove)); // create integer arraylist neighboring hexes
					
					if (tempHighlightedHexes.isEmpty()) // selected fighter has no move
						continue;
					
					temp = rand.nextInt(tempHighlightedHexes.size());
					selectedHex = tempHighlightedHexes.get(temp);
					
					testSliceHexes[selectedHex].occupation.setPlayer(p);
					testSliceHexes[selectedHex].occupation.setBase(b);
							
					for (int ff = 0; ff < tempEnemyFighters.size(); ff++){
						if (tempEnemyFighters.get(ff).getHexNum() == selectedHex){
							tempEnemyFighters.remove(ff);
							break;
						}
					}
					
					Move move = new Move(b, tempFighters.get(f).getIndex(), selectedHex, false, false);
					moveSliceList.add(move);
					tempFighters.remove(f);
					lastHexMove = selectedHex;
					
					System.out.println("selhex "+selectedHex+" ep"+ep+" eb"+eb);
				}
				}
			}
			
			// determine score of move list
			
			if (tempScore > tempBestScore){
				tempBestScore = tempScore;
				bestSliceList.clear();
				bestSliceList.addAll(moveSliceList);
			}
			
		}
		
		return bestSliceList;
	}

	void updateSliceDefendRanks(ArrayList<Fighter> tempEnemyFighters){
		for (int i = 0; i < testSliceHexes.length; i++)
			testSliceHexes[i].setDefendRank(0);
		for (int ef = 0; ef < tempEnemyFighters.size(); ef++){
			if (tempEnemyFighters.get(ef).getRank() > testSliceHexes[tempEnemyFighters.get(ef).getHexNum()].getDefendRank())
				testSliceHexes[tempEnemyFighters.get(ef).getHexNum()].setDefendRank(tempEnemyFighters.get(ef).getRank());
			for (int n = 0; n < 6; n++){
				if (testSliceHexes[tempEnemyFighters.get(ef).getHexNum()].neighbor[n] == -1)
					continue;
	
				if (tempEnemyFighters.get(ef).getRank() > testSliceHexes[testSliceHexes[tempEnemyFighters.get(ef).getHexNum()].neighbor[n]].getDefendRank())
					testSliceHexes[testSliceHexes[tempEnemyFighters.get(ef).getHexNum()].neighbor[n]].setDefendRank(tempEnemyFighters.get(ef).getRank());
			}
		}
	}
	
	ArrayList<Integer> getHexHighlights(int rank, int p, int b, int ep, int eb, Hex[] tempHexes){
		ArrayList<Integer> highlightHexes = new ArrayList<Integer>();
		
		for (int i = 0; i < tempHexes.length; i++){
			if (!tempHexes[i].isReal())
				continue;
			if (tempHexes[i].occupation.getPlayer() != ep)
				continue;
			if ((tempHexes[i].occupation.getPlayer() == ep) && (tempHexes[i].occupation.getBase() != eb))
				continue;
			
			for (int n = 0; n < 6; n++){ 
				if (tempHexes[i].neighbor[n] == -1)
					continue;
				if ((tempHexes[tempHexes[i].neighbor[n]].occupation.getPlayer() == p) && (tempHexes[tempHexes[i].neighbor[n]].occupation.getBase() == b))
					if (rank > tempHexes[tempHexes[i].neighbor[n]].getDefendRank()){
						highlightHexes.add(i);
						break;
					}
			}
		}

		return highlightHexes;
	}
	
	ArrayList<Integer> getSelectedHexHighlights(int rank, int p, int b, int ep, int eb, Hex[] tempHexes, int selectedHex){
		ArrayList<Integer> highlightHexes = new ArrayList<Integer>();

		for (int n = 0; n < 6; n++){ 
			if (tempHexes[selectedHex].neighbor[n] == -1)
				continue;
			if ((tempHexes[tempHexes[selectedHex].neighbor[n]].occupation.getPlayer() == p) && (tempHexes[tempHexes[selectedHex].neighbor[n]].occupation.getBase() == b))
				if (rank > tempHexes[tempHexes[selectedHex].neighbor[n]].getDefendRank()){
					highlightHexes.add(tempHexes[selectedHex].neighbor[n]);
					break;
				}
		}

		return highlightHexes;
	}
	
	
	int getEnemyPlayerFocus(int p, int b){
		int ep = -1;
		
		for (int i = 0; i < hexVenture.BSIZE; i++){
			if (!testHexes[i].isReal())
				continue;
			
			if ((testHexes[i].occupation.getPlayer() == p) && (testHexes[i].occupation.getBase() == b)){
				for (int ii = 0; ii < 6; ii++){
					if (testHexes[i].neighbor[ii] > -1){
						if ((testHexes[testHexes[i].neighbor[ii]].occupation.getPlayer() != p) && (testHexes[testHexes[i].neighbor[ii]].occupation.getBase() > -1)){
							ep = testHexes[testHexes[i].neighbor[ii]].occupation.getPlayer();
							
							// revise to choose best player and or human player
						}
					}
				}
			}
		}
		
		return ep;
	}
	
	int getEnemyBaseFocus(int p, int b){
		int eb = -1;
		
		for (int i = 0; i < hexVenture.BSIZE; i++){
			if (!testHexes[i].isReal())
				continue;
			
			if ((testHexes[i].occupation.getPlayer() == p) && (testHexes[i].occupation.getBase() == b)){
				for (int ii = 0; ii < 6; ii++){
					if (testHexes[i].neighbor[ii] > -1){
						if ((testHexes[testHexes[i].neighbor[ii]].occupation.getPlayer() != p) && (testHexes[testHexes[i].neighbor[ii]].occupation.getBase() > -1)){
							eb = testHexes[testHexes[i].neighbor[ii]].occupation.getBase();
							
							// revise to choose best player and or human player
						}
					}
				}
			}
		}
		
		return eb;
	}
	
	void prepareAiFighter(int p, int b, int f){
		selectedHex = -1;
	
		setHighlight(p,b,f);
		setAiMoveLists(p, b, f);
		
		// determine defensive positions
		testPlayer[p].base.get(b).divSurroundings.clear();
		testPlayer[p].base.get(b).vulSurroundings.clear();
		setMostVulnerableDivHexes(p, b); // hexes that are divisible and within a hex of enemy base hex
		setMostVulnerableDivHexesLost(p, b, testPlayer[p].base.get(b).divSurroundings); // gets amount of hexes lost per hex captured
		setMostVulnerableHexes(p, b); // hexes that are vulnerable and within a hex neighbor of enemy hex
		
		selectedHex = getBestMove(p, b, f);
		
		if ((selectedHex == -1) || (selectedHex == 0)){
			if (!intAllOwnTerList.isEmpty()){ // capture enemy non linked territory
				selectedHex = intAllOwnTerList.get(rand.nextInt(intAllOwnTerList.size()));
				testPlayer[p].base.get(b).fighter.get(f).setMove("all own");
			}else
				selectedHex = testPlayer[p].base.get(b).fighter.get(f).getHexNum();
		}
		
		setMove(p,b,f,selectedHex);
	
		hexLink();
		
		//int style = 0;
		//if (testPlayer[p].base[b].vulSurroundings.size() > 0)
		//	style = 2;

		//for (int i = 0; i < hexVenture.BSIZE; i++)
		//	if (testHexes[i].getLossPotential() > 0)
		//		style = 1;
		
		//if (style > 0){
		//	defensiveMove(p, b, f, style);
		//	return;
		//}
	}
	
	int getBestMove(int p, int b, int f){
		if ((!intOwnFighterTerList.isEmpty()) && (testPlayer[p].base.get(b).vulSurroundings.size() > 0)){	
			for (int i = 0; i < intOwnFighterTerList.size(); i++){
				if (isAffordableCombine(p,b,f,testHexes[intOwnFighterTerList.get(i)].occupation.getFighter())){
					selectedHex = intOwnFighterTerList.get(i);
					testPlayer[p].base.get(b).fighter.get(f).setMove("upgrade");
					return selectedHex;
				}
			}
		}
		
		if (!intEnemyBaseTerList.isEmpty()){ // attack base first
			
			int hexes = 0, best = 0, baseHexChoice = 0;
			for (int i = 0; i < intEnemyBaseTerList.size(); i++){
				int player = testHexes[intEnemyBaseTerList.get(i)].occupation.getPlayer();
				int base = testHexes[intEnemyBaseTerList.get(i)].occupation.getBase();
				hexes = testPlayer[player].base.get(base).getHexesLinked();
				
				if (hexes > best){
					best = hexes;
					baseHexChoice = intEnemyBaseTerList.get(i);
				}
			}
			
			selectedHex = baseHexChoice;
			testPlayer[p].base.get(b).fighter.get(f).setMove("attack base");
			return selectedHex;
		}
		
		if (!intEnemyTerDivList.isEmpty()){ // an enemy hex is divisible
			
			int hexes = 0, best = 0, hexChoice = intEnemyTerDivList.get(0);
			for (int ii = 0; ii < intEnemyTerDivList.size(); ii++){
				Hex[] tempHexes = new Hex[hexVenture.BSIZE];
				for (int i = 0; i < hexVenture.BSIZE; i++){
					tempHexes[i] = new Hex();
					tempHexes[i].setColumn(testHexes[i].getColumn());
					tempHexes[i].setRow(testHexes[i].getRow());
					tempHexes[i].setLinked(testHexes[i].isLinked());
					tempHexes[i].setReal(testHexes[i].isReal());
					tempHexes[i].occupation = new hexOccupation();
					tempHexes[i].occupation.setPlayer(testHexes[i].occupation.getPlayer());
					tempHexes[i].occupation.setBase(testHexes[i].occupation.getBase());
					tempHexes[i].occupation.setFighter(testHexes[i].occupation.getFighter());
					tempHexes[i].occupation.setFighterRank(testHexes[i].occupation.getFighterRank());
					tempHexes[i].occupation.setOccupiedBy(testHexes[i].occupation.getOccupiedBy());
					for (int n = 0; n < 6; n++)
						tempHexes[i].neighbor[n] = hexVenture.hexes[i].neighbor[n];
				}

				tempHexes[intEnemyTerDivList.get(ii)].setNew(true);
				hexes = getTempHexesLost(p, b, tempHexes);
				
				if (hexes > best){
					best = hexes;
					hexChoice = intEnemyTerDivList.get(ii);
				}
			}

			selectedHex = hexChoice;
			testPlayer[p].base.get(b).fighter.get(f).setMove("div ene");
			return selectedHex;
		}
		
		if (!intBaseLinkTerList.isEmpty()){ // no base or fighter to attack, then try to link bases
			selectedHex = intBaseLinkTerList.get(rand.nextInt(intBaseLinkTerList.size()));
			testPlayer[p].base.get(b).fighter.get(f).setMove("base link");
			return selectedHex;
		}
		
		if (testHexes[testPlayer[p].base.get(b).getHexNum()].getDefendRank() < testHexes[testPlayer[p].base.get(b).getHexNum()].getThreatRank()){
			if (testPlayer[p].base.get(b).fighter.get(f).getRank() >= testHexes[testPlayer[p].base.get(b).getHexNum()].getThreatRank()){
				for (int i=0;i<6;i++){
					if (testHexes[testPlayer[p].base.get(b).getHexNum()].neighbor[i] >= 0){
						if (testHexes[testHexes[testPlayer[p].base.get(b).getHexNum()].neighbor[i]].isHighlighted()){
							selectedHex = testHexes[testPlayer[p].base.get(b).getHexNum()].neighbor[i];
							testPlayer[p].base.get(b).fighter.get(f).setMove("defend base");
							return selectedHex;
						}
					}
				}
			}
		}
		
		if (!intEnemyBaseNeighborTerList.isEmpty()){ // attack base first
			int hexes, best = 0, hexChoice = 0, pl = 0, ba = 0;
			for (int i = 0; i < intEnemyBaseNeighborTerList.size(); i++){
				for (int n = 0; n < 6; n++){
					if (testHexes[intEnemyBaseNeighborTerList.get(i)].neighbor[n] > -1){
						if (testHexes[testHexes[intEnemyBaseNeighborTerList.get(i)].neighbor[n]].occupation.getPlayer() != p){
							if (testHexes[testHexes[intEnemyBaseNeighborTerList.get(i)].neighbor[n]].occupation.getOccupiedBy() == "base"){
								pl = testHexes[testHexes[intEnemyBaseNeighborTerList.get(i)].neighbor[n]].occupation.getPlayer();
								ba = testHexes[testHexes[intEnemyBaseNeighborTerList.get(i)].neighbor[n]].occupation.getBase();
								break;
							}
						}
					}
				}
				hexes = testPlayer[pl].base.get(ba).getHexesLinked();
				
				if (hexes > best){
					best = hexes;
					hexChoice = intEnemyBaseNeighborTerList.get(i);
				}
			}
			selectedHex = hexChoice;
			testPlayer[p].base.get(b).fighter.get(f).setMove("att nei base");
			return selectedHex;
		}
		
		if (!intEnemyFighterTerList.isEmpty()){ // if no base to attack, attack a fighter
			int rank, bestRank = 0, pl, ba, fi, hexChoice = 0;
			for (int i = 0; i < intEnemyFighterTerList.size(); i++){
				pl = testHexes[intEnemyFighterTerList.get(i)].occupation.getPlayer();
				ba = testHexes[intEnemyFighterTerList.get(i)].occupation.getBase();
				fi = testHexes[intEnemyFighterTerList.get(i)].occupation.getFighter();
				rank = testPlayer[pl].base.get(ba).fighter.get(fi).getRank();
				
				if (rank > bestRank){
					bestRank = rank;
					hexChoice = intEnemyFighterTerList.get(i);
				}
			}
			selectedHex = hexChoice;
			testPlayer[p].base.get(b).fighter.get(f).setMove("ene fi");
			return selectedHex;
		}
		
		if (!intEnemyTerDivNeighborList.isEmpty()){ // an enemy divisible hex neighbor
			selectedHex = intEnemyTerDivNeighborList.get(rand.nextInt(intEnemyTerDivNeighborList.size()));
			testPlayer[p].base.get(b).fighter.get(f).setMove("div nei ene");
			return selectedHex;
		}
		
		if (!intEnemyFighterNeighborTerList.isEmpty()){ // if no base to attack, attack a fighter
			int rank, bestRank = 0, hexChoice = 0;
			for (int i = 0; i < intEnemyFighterNeighborTerList.size(); i++){
				rank = testHexes[intEnemyFighterNeighborTerList.get(i)].getThreatRank();
				
				if (rank > bestRank){
					bestRank = rank;
					hexChoice = intEnemyFighterNeighborTerList.get(i);
				}
			}
			selectedHex = hexChoice;
			testPlayer[p].base.get(b).fighter.get(f).setMove("ene nei fi");
			return selectedHex;
		}
		
		if (!intEnemyTerList.isEmpty()){ // no base or fighter to attack, then capture enemy territory
			for (int i = 0; i < intEnemyTerList.size(); i++){
				if (testPlayer[p].base.get(b).vulSurroundings.size() > 0){
					for (int ii = 0; ii < testPlayer[p].base.get(b).vulSurroundings.size(); ii++){
						for (int n = 0; n < 6; n++){
							if (testHexes[testPlayer[p].base.get(b).vulSurroundings.get(ii)].neighbor[n] > -1){
								if (testHexes[testPlayer[p].base.get(b).vulSurroundings.get(ii)].neighbor[n] == intEnemyTerList.get(i)){
									selectedHex = intEnemyTerList.get(i);
									testPlayer[p].base.get(b).fighter.get(f).setMove("cap vul ene linked");
									return selectedHex;
								}
							}
						}
					}
				}
			}
			selectedHex = intEnemyTerList.get(rand.nextInt(intEnemyTerList.size()));
			testPlayer[p].base.get(b).fighter.get(f).setMove("ene linked");
			return selectedHex;
		}
		
		if (!intNeutralTerList.isEmpty()){ // capture enemy non linked territory
			for (int i = 0; i < intNeutralTerList.size(); i++){
				if (testPlayer[p].base.get(b).vulSurroundings.size() > 0){
					for (int ii = 0; ii < testPlayer[p].base.get(b).vulSurroundings.size(); ii++){
						for (int n = 0; n < 6; n++){
							if (testHexes[testPlayer[p].base.get(b).vulSurroundings.get(ii)].neighbor[n] > -1){
								if (testHexes[testPlayer[p].base.get(b).vulSurroundings.get(ii)].neighbor[n] == intNeutralTerList.get(i)){
									selectedHex = intNeutralTerList.get(i);
									testPlayer[p].base.get(b).fighter.get(f).setMove("cap vul ene");
									return selectedHex;
								}
							}
						}
					}
				}
			}
			selectedHex = intNeutralTerList.get(rand.nextInt(intNeutralTerList.size()));
			testPlayer[p].base.get(b).fighter.get(f).setMove("cap ene");
			return selectedHex;
		}
		
		if (!intOwnFalseTerList.isEmpty()){ // capture neighboring territory to non linked territory
			for (int i = 0; i < intOwnFalseTerList.size(); i++){
				if (testPlayer[p].base.get(b).vulSurroundings.size() > 0){
					for (int ii = 0; ii < testPlayer[p].base.get(b).vulSurroundings.size(); ii++){
						for (int n = 0; n < 6; n++){
							if (testHexes[testPlayer[p].base.get(b).vulSurroundings.get(ii)].neighbor[n] > -1){
								if (testHexes[testPlayer[p].base.get(b).vulSurroundings.get(ii)].neighbor[n] == intOwnFalseTerList.get(i)){
									selectedHex = intOwnFalseTerList.get(i);
									testPlayer[p].base.get(b).fighter.get(f).setMove("cap vul nei own");
									return selectedHex;
								}
							}
						}
					}
				}
			}
			selectedHex = intOwnFalseTerList.get(rand.nextInt(intOwnFalseTerList.size()));
			testPlayer[p].base.get(b).fighter.get(f).setMove("cap nei own");
			return selectedHex;
		}
		
		if (!intAllOwnTerList.isEmpty()){ // capture enemy non linked territory
			selectedHex = intAllOwnTerList.get(rand.nextInt(intAllOwnTerList.size()));
			testPlayer[p].base.get(b).fighter.get(f).setMove("all own");
			return selectedHex;
		}
		
		return -1;
	}
	
	void defensiveMove(int p, int b, int f, int style){
		if (style == 1){
		boolean placeNeighbor = false;
		for (int l = 1; l < 10; l++){ // place fighters on divisible hexes based off order of impact (hexes lost)
			for (int i = 0; i < hexVenture.BSIZE; i++){ // place fighter on most vulnerable divisible hex which will lose most hexes if captured
				if ((testHexes[i].getLossPotential() == l) && (testHexes[i].getLossPotential() > 0)){
					placeNeighbor = false;
					for (int n = 0; n < 6; n++){ // place fighter on a capturable non owned hex neighbor of divisible hex ideally
						if (testHexes[i].neighbor[n] >= 0){
							if (testHexes[testHexes[i].neighbor[n]].occupation.getPlayer() != p){
								if (testHexes[testHexes[i].neighbor[n]].isHighlighted){
									selectedHex = testHexes[i].neighbor[n];
									placeNeighbor = true;
									break;
								}
							}
						}
					}
					//if (hexes[i].isHighlighted){ // otherwise the actual divisible hex
					if (!placeNeighbor)
						selectedHex = i;
					
					break;
					
					//}
				}
			}
			if (selectedHex > -1){
				testPlayer[p].base.get(b).fighter.get(f).setMove("def div");
				break;
			}
		}
		}else if (style == 2){
			selectedHex = testPlayer[p].base.get(b).vulSurroundings.get(rand.nextInt(testPlayer[p].base.get(b).vulSurroundings.size()));
			testPlayer[p].base.get(b).fighter.get(f).setMove("def vul");
		}
	}
	
	int purchaseFighter(int p, int b){
		Fighter tempFighter = new Fighter();
		testPlayer[p].base.get(b).fighter.add(tempFighter);
		int f = testPlayer[p].base.get(b).fighter.size()-1;
		testPlayer[p].base.get(b).fighter.get(f).setHexNum(testPlayer[p].base.get(b).getHexNum());
		testPlayer[p].base.get(b).fighter.get(f).setMoved(false);
		testPlayer[p].base.get(b).fighter.get(f).setX(testPlayer[p].base.get(b).getCol());
		testPlayer[p].base.get(b).fighter.get(f).setY(testPlayer[p].base.get(b).getRow());
		testPlayer[p].base.get(b).fighter.get(f).setRank(1);
		testPlayer[p].base.get(b).fighter.get(f).setBaseNum(b);
		testPlayer[p].base.get(b).setMoney(-hexVenture.FIGHTER_RANK_ONE_EXPENSE*5);
		testPlayer[p].base.get(b).fighter.get(f).setCost(hexVenture.FIGHTER_RANK_ONE_EXPENSE);
		return f;
	}
	
	void setHighlight(int p, int b, int f){
		
		for (int h=0;h<hexVenture.BSIZE;h++)
			testHexes[h].setHighlighted(false);
		
		for (int h=0;h<hexVenture.BSIZE;h++){
			if ((testHexes[h].occupation.getOccupiedBy()!="bonus")){
				if ((testHexes[h].occupation.getPlayer() == p) && (testHexes[h].isLinked()) && (testHexes[h].isReal())){
					if (testHexes[h].occupation.getBase()==b){
						testHexes[h].setHighlighted(true);
						int n=0;
						for (int i=0;i<6;i++){ 
							n=testHexes[h].neighbor[i];
							if (n>=0)
								if ((testHexes[h].isHighlighted())&&(testHexes[n].isReal()))
									testHexes[n].setHighlighted(true);
						}
					}
				}
			}
		}
		
		for (int h=0;h<hexVenture.BSIZE;h++)
			checkHexOccupation(p,b,testPlayer[p].base.get(b).fighter.get(f).getRank(),h);
		
		for (int h=0;h<hexVenture.BSIZE;h++)
			checkHexOccupationExtra(p,b,testPlayer[p].base.get(b).fighter.get(f).getRank(),h);
		
	}
	
	boolean availableHighlight(int p, int b){
		for (int h=0;h<hexVenture.BSIZE;h++)
			testHexes[h].setHighlighted(false);
		
		for (int h=0;h<hexVenture.BSIZE;h++){
			if ((testHexes[h].occupation.getOccupiedBy()!="bonus")){
				if ((testHexes[h].occupation.getPlayer() == p) && (testHexes[h].isLinked()) && (testHexes[h].isReal())){
					if (testHexes[h].occupation.getBase()==b){
						testHexes[h].setHighlighted(true);
						int n=0;
						for (int i=0;i<6;i++){ 
							n=testHexes[h].neighbor[i];
							if (n>=0)
								if ((testHexes[h].isHighlighted())&&(testHexes[n].isReal()))
									testHexes[n].setHighlighted(true);
						}
					}
				}
			}
		}
		
		for (int h=0;h<hexVenture.BSIZE;h++)
			checkHexOccupation(p,b,1,h);
		
		for (int h=0;h<hexVenture.BSIZE;h++)
			checkHexOccupationExtra(p,b,1,h);
		
		for (int i = 0; i < hexVenture.BSIZE; i++)
			if (testHexes[i].isHighlighted())
				return true;
		
		return false;
	}
	
	void checkHexOccupation(int p, int b, int fRank, int n){
		int pp,bb,ff;
		if ((testHexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) || (testHexes[n].occupation.getFighter() > -1)){
			pp=testHexes[n].occupation.getPlayer();
			bb=testHexes[n].occupation.getBase();
			ff=testHexes[n].occupation.getFighter();

			if ((testHexes[n].occupation.getPlayer() != p) && (testHexes[n].isHighlighted())){ // occupied by enemy
				if (testHexes[n].occupation.getFighter() > -1){ // occupied by a fighter
					if (fRank>testPlayer[pp].base.get(bb).fighter.get(ff).getRank())
						testHexes[n].setHighlighted(true);
					else
						testHexes[n].setHighlighted(false);
				}
				if ((testHexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) && (testHexes[n].isHighlighted())){ // occupied by a base
					if (fRank>testPlayer[pp].base.get(bb).getRank())
						testHexes[n].setHighlighted(true);
					else
						testHexes[n].setHighlighted(false);
				}
			}
			if (testHexes[n].occupation.getPlayer() == p) // occupied by own
				if (testHexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) // occupied by a base
					testHexes[n].setHighlighted(false);
	}
	}
	
	void checkHexOccupationExtra(int p, int b, int fRank, int n){
		int pp,bb,ff;
		if ((testHexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) || (testHexes[n].occupation.getFighter() > -1)){
			pp=testHexes[n].occupation.getPlayer();
			bb=testHexes[n].occupation.getBase();
			ff=testHexes[n].occupation.getFighter();
			
			if (testHexes[n].occupation.getPlayer() != p){ // occupied by enemy
				if (testHexes[n].occupation.getFighter() > -1) // occupied by a fighter
					if (fRank <= testPlayer[pp].base.get(bb).fighter.get(ff).getRank())
						for (int nn=0;nn<6;nn++)
							if (testHexes[n].neighbor[nn]>-1)
								if (testHexes[testHexes[n].neighbor[nn]].occupation.getPlayer() == pp)
									testHexes[testHexes[n].neighbor[nn]].setHighlighted(false);

			if (testHexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) // occupied by a base
				if (fRank<=testPlayer[pp].base.get(bb).getRank())
					for (int nn=0;nn<6;nn++)
						if (testHexes[n].neighbor[nn]>-1)
							if (testHexes[testHexes[n].neighbor[nn]].occupation.getPlayer() == pp)
								testHexes[testHexes[n].neighbor[nn]].setHighlighted(false);	
			}
		}
	}
	
	void setMove(int p, int b, int f, int h){
		int ff;
			
		hexVenture.selectedPlayer=p;
		hexVenture.selectedBase=b;
		hexVenture.selectedFighter=f;

		for (int i = 0; i < testPlayer[p].base.get(b).fighter.size(); i++)
			testPlayer[p].base.get(b).fighter.get(i).setSelected(false);
		
		testHexes[testPlayer[p].base.get(b).fighter.get(f).getHexNum()].occupation.setFighter(-1);
		testPlayer[p].base.get(b).fighter.get(f).setSelected(true);

		if (testHexes[h].occupation.getPlayer() != p){
			if (testHexes[h].occupation.getFighter() > -1)
				killFighter(testHexes[h].occupation.getPlayer(), testHexes[h].occupation.getBase(), testHexes[h].occupation.getFighter());
			else if (testHexes[h].occupation.getOccupiedBy().equalsIgnoreCase("base"))
				sackBaseVerNew(p,b,testHexes[h].occupation.getPlayer(),testHexes[h].occupation.getBase());
		}
		else if (testHexes[h].occupation.getPlayer() == p){
			if (testHexes[h].occupation.getFighter() > -1){
				if (testHexes[h].occupation.getFighter() != f){
					ff=testHexes[h].occupation.getFighter();
					combineFighters(p,b,f,ff);
					return;
				}
			}
		}
		
		for (int fff = 0; fff < testPlayer[p].base.get(hexVenture.selectedBase).fighter.size(); fff++){
			if (testPlayer[p].base.get(hexVenture.selectedBase).fighter.get(fff).isSelected()){
				hexVenture.selectedFighter = fff;
				break;
			}
		}

		testPlayer[p].base.get(hexVenture.selectedBase).fighter.get(hexVenture.selectedFighter).setSelected(false);
		
		placeFighterOnHex(h,p,hexVenture.selectedBase,hexVenture.selectedFighter,testHexes[h].getColumn(),testHexes[h].getRow());
	}
	
	void setAiMoveLists(int p, int b, int f){ // priority
		intBaseLinkTerList.clear();
		intAllOwnTerList.clear();
		intOwnTerList.clear(); // 7
		intOwnFalseTerList.clear(); // 7
		intOwnFighterTerList.clear(); // 6
		intNeutralTerList.clear(); // 5
		intbonusLinkTerList.clear(); // 4
		intEnemyTerList.clear(); // 3
		intEnemyTerDivList.clear();
		intEnemyTerDivNeighborList.clear();
		intEnemyFighterTerList.clear(); // 2
		intEnemyFighterNeighborTerList.clear(); // 2
		intEnemyBaseTerList.clear(); // 1
		intEnemyBaseNeighborTerList.clear(); // 1
		
		for (int h=0;h<hexVenture.BSIZE;h++){
			if (testHexes[h].isHighlighted()){
				
				for (int n=0;n<6;n++){
					if (testHexes[h].neighbor[n]>=0){
						if (testHexes[testHexes[h].neighbor[n]].isLinked()){
							if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer()==p){
								if (testHexes[testHexes[h].neighbor[n]].occupation.getBase()!=b){
									intBaseLinkTerList.add(h);
									break;
								}
							}
						}
					}
				}
				
				
				if (testHexes[h].occupation.getPlayer() == p){
					intAllOwnTerList.add(h);
					
					//if (testHexes[h].hasFalseIncome)
					if (testHexes[h].occupation.getBase() > -1){
						for (int n=0;n<6;n++)
							if (testHexes[h].neighbor[n]>=0)
								if ((testHexes[testHexes[h].neighbor[n]].occupation.getBase() == -1) && (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() == p))		
									intOwnFalseTerList.add(testHexes[h].neighbor[n]);
					
					if (testHexes[h].occupation.getFighter() > -1){
						if ((testHexes[h].occupation.getFighter() != f) && (testPlayer[p].base.get(testHexes[h].occupation.getBase()).fighter.get(testHexes[h].occupation.getFighter()).getRank()<4))
							if (testPlayer[p].base.get(testHexes[h].occupation.getBase()).fighter.get(testHexes[h].occupation.getFighter()).isMoved())
								if (testHexes[h].getDefendRank() <= testHexes[h].getThreatRank())
										intOwnFighterTerList.add(h);
					}
					}
				}
				
				if (testHexes[h].occupation.getPlayer() != p)
					if (testHexes[h].occupation.getBase() == -1)
						intNeutralTerList.add(h);
			
				if (testHexes[h].occupation.getPlayer() != p){
					for (int n=0;n<6;n++){
						if (testHexes[h].neighbor[n]>=0){
							if (testHexes[testHexes[h].neighbor[n]].getRegionBonus() > 0){
								intbonusLinkTerList.add(h);
							}
						}
					}
				}
			
				if ((testHexes[h].occupation.getPlayer() != p) && (testHexes[h].occupation.getBase() > -1)){
					intEnemyTerList.add(h);
					int pp = testHexes[h].occupation.getPlayer();
					if (isHexDivisible(pp, h)){
						intEnemyTerDivList.add(h);
						addHexDivisibleNeighbors(h);
					}
					if (testHexes[h].occupation.getFighter() > -1){
						intEnemyFighterTerList.add(h);
					}
					if (testHexes[h].occupation.getOccupiedBy()=="base"){
						intEnemyBaseTerList.add(h);
					}
				}
				
				if ((testHexes[h].occupation.getPlayer() != p)){
					
					for (int n=0;n<6;n++){
						if (testHexes[h].neighbor[n]>=0){
							if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
							if (testHexes[testHexes[h].neighbor[n]].occupation.getFighter() > -1){
								intEnemyFighterNeighborTerList.add(h);
								break;
							}
							}
						}
					}
					
					for (int n=0;n<6;n++){
						if (testHexes[h].neighbor[n]>=0){
							if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
							if (testHexes[testHexes[h].neighbor[n]].occupation.getOccupiedBy()=="base"){
								intEnemyBaseNeighborTerList.add(h);
								break;
							}
							}
						}
					}
				}
				
			}
		}
	}
	
	public boolean isHexDivisible(int p, int h){
		int n = 0, flipTally = 0;
		boolean flip = false, prevFlip = false;
		
		if (testHexes[h].neighbor[n] < 0)
			flip = false;
		if (testHexes[h].neighbor[n] > -1){
			if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 2;
		prevFlip = flip;
			
		if (testHexes[h].neighbor[n] < 0)
			flip = false;
		if (testHexes[h].neighbor[n] > -1){
			if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 3;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
			
		if (testHexes[h].neighbor[n] < 0)
			flip = false;
		if (testHexes[h].neighbor[n] > -1){
			if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 1;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
		
		if (testHexes[h].neighbor[n] < 0)
			flip = false;
		if (testHexes[h].neighbor[n] > -1){
			if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 4;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
		
		if (testHexes[h].neighbor[n] < 0)
			flip = false;
		if (testHexes[h].neighbor[n] > -1){
			if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 5;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
		
		if (testHexes[h].neighbor[n] < 0)
			flip = false;
		if (testHexes[h].neighbor[n] > -1){
			if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		if (prevFlip != flip)
			flipTally++;
		
		if (flipTally >= 3)
			return true;
		
		return false;
	}
	
	public void addHexDivisibleNeighbors(int h){
		for (int n = 0; n < 6; n++)
			if (testHexes[h].neighbor[n] > -1)
				if (testHexes[testHexes[h].neighbor[n]].isHighlighted())
					intEnemyTerDivNeighborList.add(testHexes[h].neighbor[n]);
	}
	
	public void setHexThreatRank(int p){
		for (int h = 0; h < hexVenture.BSIZE; h++){
			if ((testHexes[h].occupation.getPlayer() > -1) && (testHexes[h].occupation.getPlayer() != p) && (testHexes[h].occupation.getBase() > -1)){
				for (int n=0;n<6;n++){
					if (testHexes[h].neighbor[n]>=0){
						//if ((hexes[hexes[h].neighbor[n]].occupation.getPlayer() == p) && (hexes[hexes[h].neighbor[n]].occupation.getBase() > -1)){
							if (testHexes[testHexes[h].neighbor[n]].getThreatRank() < testPlayer[testHexes[h].occupation.getPlayer()].base.get(testHexes[h].occupation.getBase()).getHighestRankedFighter()){
								testHexes[testHexes[h].neighbor[n]].setThreatRank(testPlayer[testHexes[h].occupation.getPlayer()].base.get(testHexes[h].occupation.getBase()).getHighestRankedFighter());
							}
						//}
							for (int nn=0;nn<6;nn++){
								if (testHexes[testHexes[h].neighbor[n]].neighbor[nn]>=0){
									//if ((hexes[hexes[h].neighbor[n]].occupation.getPlayer() == p) && (hexes[hexes[h].neighbor[n]].occupation.getBase() > -1)){
										if (testHexes[testHexes[testHexes[h].neighbor[n]].neighbor[nn]].getThreatRank() < testPlayer[testHexes[h].occupation.getPlayer()].base.get(testHexes[h].occupation.getBase()).getHighestRankedFighter()){
											testHexes[testHexes[testHexes[h].neighbor[n]].neighbor[nn]].setThreatRank(testPlayer[testHexes[h].occupation.getPlayer()].base.get(testHexes[h].occupation.getBase()).getHighestRankedFighter());
										}
									//}
								}
							}
					}
				}
			}
		}
	}
	
	public void setMostVulnerableHexes(int p, int b){
		for (int h = 0; h < hexVenture.BSIZE; h++){
			if (!testHexes[h].isReal())
				continue;
			if (testHexes[h].occupation.getOccupiedBy() == "base")
				continue;
			if (testHexes[h].occupation.getPlayer() != p)
				continue;
			if (testHexes[h].occupation.getBase() != b)
				continue;

			if (testHexes[h].getDefendRank() < testHexes[h].getThreatRank())
				if (!testHexes[h].isFighterCover())
					testPlayer[p].base.get(b).vulSurroundings.add(h);
			
			testHexes[h].setSumRank(testHexes[h].getDefendRank() - testHexes[h].getThreatRank());
		}
	}
	
	public void setMostVulnerableDivHexes(int p, int b){
		boolean goodPlace;
		for (int h = 0; h < hexVenture.BSIZE; h++){
			if (!testHexes[h].isReal())
				continue;
			if ((testHexes[h].occupation.getPlayer() == p) && (testHexes[h].occupation.getOccupiedBy() == "base"))
				continue;
			goodPlace = false;

				if ((testHexes[h].occupation.getPlayer() == p) && (testHexes[h].occupation.getBase() == b)){
					for (int nn=0;nn<6;nn++){
						if (testHexes[h].neighbor[nn]>=0){
							if ((testHexes[testHexes[h].neighbor[nn]].occupation.getPlayer() != p) && (testHexes[testHexes[h].neighbor[nn]].occupation.getPlayer() > -1)){
								if (isHexDivisible(p, h)){ 
									if ((testHexes[h].getDefendRank() == 0) || (testHexes[h].getThreatRank() > testHexes[h].getDefendRank())) // is the hex defended well enough?
										goodPlace = true;
									//else if ((testHexes[h].occupation.getFighter() > -1) && ((testHexes[h].getDefendRank() > 0) || (testHexes[h].getThreatRank() <= testHexes[h].getDefendRank()))){
										//testPlayer[testHexes[h].occupation.getPlayer()].base.get(testHexes[h].occupation.getBase()).fighter.get(testHexes[h].occupation.getFighter()).setMoved(true);
									//}
								}
							}
						}
					}
				}
		
				// whether a fighter is on this hex or not, it's not defended enough, so simply don't place on a hex that is a base
			if ((goodPlace) && (testHexes[h].occupation.getOccupiedBy() != "base"))
				testPlayer[p].base.get(b).divSurroundings.add(h);
		}
	}
	
	public void setMostVulnerableDivHexesLost(int p, int b, ArrayList<Integer>surroundings){
		class ListMost {
			int hex = 0, lost = 0;
			ListMost(){
			}
			public int getHex() {
				return hex;
			}
			public void setHex(int hex) {
				this.hex = hex;
			}
			public int getLost() {
				return lost;
			}
			public void setLost(int lost) {
				this.lost = lost;
			}
		}

		ListMost[] listMost = new ListMost[hexVenture.BSIZE];
		
		for (int i = 0; i < hexVenture.BSIZE; i++)
			listMost[i] = new ListMost();
			
		//int inc = -1;
		//for (int i = 0; i < BSIZE; i++)
			//if ((testHexes[i].occupation.getPlayer() == p) && (testHexes[i].occupation.getBase() == b) && (testHexes[i].occupation.getOccupiedBy() == "empty"))
				//listMost[++inc].setHex(i);
		
		int hexesLost = 0;
		Hex[] tempHexes = new Hex[hexVenture.BSIZE];
		for (int i = 0; i < hexVenture.hexes.length; i++){
			tempHexes[i] = new Hex();
			tempHexes[i].setColumn(testHexes[i].getColumn());
			tempHexes[i].setRow(testHexes[i].getRow());
			tempHexes[i].setLinked(testHexes[i].isLinked());
			tempHexes[i].setReal(testHexes[i].isReal());
			tempHexes[i].occupation = new hexOccupation();
			tempHexes[i].occupation.setPlayer(testHexes[i].occupation.getPlayer());
			tempHexes[i].occupation.setBase(testHexes[i].occupation.getBase());
			tempHexes[i].occupation.setFighter(testHexes[i].occupation.getFighter());
			tempHexes[i].occupation.setFighterRank(testHexes[i].occupation.getFighterRank());
			tempHexes[i].occupation.setOccupiedBy(testHexes[i].occupation.getOccupiedBy());
			for (int n = 0; n < 6; n++)
				tempHexes[i].neighbor[n] = hexVenture.hexes[i].neighbor[n];
		}

		for (int h = 0; h < hexVenture.BSIZE; h++)
			testHexes[h].setLossPotential(0);
		
		for (int i = 0; i < surroundings.size(); i++){
			tempHexes[surroundings.get(i)].setNew(true);
			hexesLost = getTempHexesLost(p, b, tempHexes);
			
			for (int ii = 0; ii < listMost.length-1; ii++){
				if (hexesLost > listMost[ii].getLost()){
					for (int iii = listMost.length-1; iii > ii; iii--){
						listMost[iii].setHex(listMost[iii-1].getHex());
						listMost[iii].setLost(listMost[iii-1].getLost());
					}
					listMost[ii].setHex(surroundings.get(i));
					listMost[ii].setLost(hexesLost);
					break;
				}
			}

			tempHexes[surroundings.get(i)].setNew(false);
			
			for (int ii = 0; ii < hexVenture.BSIZE; ii++)
				testHexes[listMost[ii].getHex()].setLossPotential(ii+1);
		}
		
		
	}
	
	public int getTempHexesLost(int p, int b, Hex[] tempHexes) { // determine linked hexes
		int cur = testPlayer[p].base.get(b).getHexesLinked(), tally = 0, lost = 0;
		
		for (int hex=0;hex<(hexVenture.BSIZE);hex++)
			tempHexes[hex].setLinked(false); 
			
		for (int hex=0;hex<(hexVenture.BSIZE);hex++)
			tempHexes[hex].setTallyLinked(false);
		
		tempHexes[testPlayer[p].base.get(b).getHexNum()].setTallyLinked(true);
		tempHexes[testPlayer[p].base.get(b).getHexNum()].setLinked(true);
					
				int iWork=0;
				do{iWork++;
					int hex=0;
					int row=-1;
					int col=0;
					do{ row++;
						hex=row;
						do{ col++;
						if (!tempHexes[hex].isReal())
							continue;
						if (tempHexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((tempHexes[hex].isLinked())&&(tempHexes[hex].occupation.getBase()==b)){
									if (tempHexes[hex].neighbor[iii]==-1)
										continue;
				
									if (tempHexes[tempHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										tempHexes[tempHexes[hex].neighbor[iii]].setTallyLinked(true);
										tempHexes[tempHexes[hex].neighbor[iii]].setLinked(true);
									}
								}
							}while(iii<5);
						}
						hex+=hexVenture.ROWSIZE;
						} while (col<hexVenture.COLSIZE);
					} while (row<(hexVenture.ROWSIZE-1));
				
					row=hexVenture.ROWSIZE;
					col=0;
					do{ row--;
						hex=row+((hexVenture.COLSIZE-1)*hexVenture.ROWSIZE);
						do{ col++;
						if (!tempHexes[hex].isReal())
							continue;
						if (tempHexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((tempHexes[hex].isLinked())&&(tempHexes[hex].occupation.getBase()==b)){
									if (tempHexes[hex].neighbor[iii]==-1)
										continue;
									
									if (tempHexes[tempHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										tempHexes[tempHexes[hex].neighbor[iii]].setTallyLinked(true);
										tempHexes[tempHexes[hex].neighbor[iii]].setLinked(true);
									}
								}
							}while(iii<5);
						}
						hex-=hexVenture.ROWSIZE;
						} while (col<hexVenture.COLSIZE);
					} while (row>0);
					
					//-------------------------------
					
					hex=-1;
					do{ hex++;
					if (!tempHexes[hex].isReal())
						continue;
				    	if (tempHexes[hex].occupation.getPlayer() == p){
				    		int iii=-1;
				    		do{iii++;
				    		if ((tempHexes[hex].isLinked())&&(tempHexes[hex].occupation.getBase()==b)){
				    			if (tempHexes[hex].neighbor[iii]==-1)
									continue;
								
				    			if (tempHexes[tempHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
				    				tempHexes[tempHexes[hex].neighbor[iii]].setTallyLinked(true);
				    				tempHexes[tempHexes[hex].neighbor[iii]].setLinked(true);
				    			}
				    		}
				    		}while(iii<5);

				    	}
					} while (hex<(hexVenture.BSIZE)-1);
				
					hex=hexVenture.BSIZE;
					
					do{ hex--;
					if (!tempHexes[hex].isReal())
						continue;
					if (tempHexes[hex].occupation.getPlayer() == p){
			    		int iii=-1;
			    		do{iii++;
			    		if ((tempHexes[hex].isLinked())&&(tempHexes[hex].occupation.getBase()==b)){
			    			if (tempHexes[hex].neighbor[iii]==-1)
								continue;
							
			    			if (tempHexes[tempHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
			    				tempHexes[tempHexes[hex].neighbor[iii]].setTallyLinked(true);
			    				tempHexes[tempHexes[hex].neighbor[iii]].setLinked(true);
			    			}
			    		}
			    		}while(iii<5);

			    	}
					
					if (iWork==5){
		    			if (tempHexes[hex].occupation.getPlayer() == p){
		    				if (tempHexes[hex].isTallyLinked()){
		    					if (tempHexes[hex].occupation.getBase()==b)
		    						tally++;
		    				}
		    			}
		    		}
					
					} while (hex>0);
					
				}while(iWork<6);
				
		lost = cur - tally;
		
		return lost;
	}
	
	boolean isAffordableCombine(int p, int b, int f, int ff){
		boolean isAffordable = false;
		int tempExpense=0, tempIncome=0, tempRank=0, tempCost=0;
		
		tempIncome=testPlayer[p].base.get(b).getHexesLinked()+testPlayer[p].base.get(b).getBonus();
		tempRank=(testPlayer[p].base.get(b).fighter.get(f).getRank()+testPlayer[p].base.get(b).fighter.get(ff).getRank());
		
		if (tempRank>4)
			tempRank=4;
		
		if (tempRank==2)
			tempCost=hexVenture.FIGHTER_RANK_TWO_EXPENSE;
		else if (tempRank==3)
			tempCost=hexVenture.FIGHTER_RANK_THREE_EXPENSE;
		else if (tempRank==4)
			tempCost=hexVenture.FIGHTER_RANK_FOUR_EXPENSE;
		
		for (int fighter = 0; fighter < testPlayer[p].base.get(b).fighter.size(); fighter++){
			if ((fighter!=f)&&(fighter!=ff))
				tempExpense+=testPlayer[p].base.get(b).fighter.get(fighter).getCost();
		}
		tempExpense+=tempCost;
		
		if (tempIncome>=tempExpense)
			isAffordable=true;
		
		return isAffordable;
	}
	
	void combineFighters(int p, int b, int f1, int f2){
		testPlayer[p].base.get(b).fighter.get(f1).setRank(testPlayer[p].base.get(b).fighter.get(f1).getRank()+testPlayer[p].base.get(b).fighter.get(f2).getRank());
		
		if (testPlayer[p].base.get(b).fighter.get(f1).getRank()>4)
			testPlayer[p].base.get(b).fighter.get(f1).setRank(4);
		
		if (testPlayer[p].base.get(b).fighter.get(f1).getRank()==2)
			testPlayer[p].base.get(b).fighter.get(f1).setCost(hexVenture.FIGHTER_RANK_TWO_EXPENSE);
		else if (testPlayer[p].base.get(b).fighter.get(f1).getRank()==3)
			testPlayer[p].base.get(b).fighter.get(f1).setCost(hexVenture.FIGHTER_RANK_THREE_EXPENSE);
		else if (testPlayer[p].base.get(b).fighter.get(f1).getRank()==4)
			testPlayer[p].base.get(b).fighter.get(f1).setCost(hexVenture.FIGHTER_RANK_FOUR_EXPENSE);
		
		if (testPlayer[p].base.get(b).fighter.get(f2).isMoved())
			testPlayer[p].base.get(b).fighter.get(f1).setMoved(true);
		testHexes[testPlayer[p].base.get(b).fighter.get(f2).getHexNum()].occupation.setFighter(f1);
		testHexes[testPlayer[p].base.get(b).fighter.get(f2).getHexNum()].occupation.setFighterRank(testPlayer[p].base.get(b).fighter.get(f1).getRank());
		
		testHexes[testPlayer[p].base.get(b).fighter.get(f1).getHexNum()].occupation.setFighter(-1);
		
		testPlayer[p].base.get(b).fighter.get(f1).setHexNum(testPlayer[p].base.get(b).fighter.get(f2).getHexNum());
		
		setFighterGridDefend(p, b, f2, testPlayer[p].base.get(b).fighter.get(f2).getHexNum());
		
		testPlayer[p].base.get(b).fighter.remove(f2);
		
		for (int i = 0; i < testPlayer[p].base.get(b).fighter.size(); i++)
			testHexes[testPlayer[p].base.get(b).fighter.get(i).getHexNum()].occupation.setFighter(i);
		
		for (int fff = 0; fff < testPlayer[p].base.get(b).fighter.size(); fff++){
			if (testPlayer[p].base.get(b).fighter.get(fff).isSelected()){
				hexVenture.selectedFighter = fff;
				testPlayer[p].base.get(b).fighter.get(fff).setSelected(false);
				break;
			}
		}
	}
	
	void sackBaseVerOld(int p, int b, int pp, int bb){
		int baseBig=0,base=0;

		if (!testPlayer[pp].isAI())
			score+=15;
		
		for (int h=0;h<hexVenture.BSIZE;h++){
			if ((testHexes[h].occupation.getPlayer() == pp)&&(testHexes[h].occupation.getBase()==bb)){
				testHexes[h].occupation.setPlayer(p);
				testHexes[h].occupation.setBase(b);
			}
		}
	
		killSackedBaseFighters(pp,bb);
		testHexes[testPlayer[pp].base.get(bb).getHexNum()].occupation.setOccupiedBy("empty");
		testPlayer[pp].base.remove(bb);
	
		for (int i = 0; i < hexVenture.BSIZE; i++)
			if ((testHexes[i].occupation.getPlayer() == pp) && (testHexes[i].occupation.getBase() > -1))
				testHexes[i].occupation.setBase(-1);
	
		for (int i = 0; i < testPlayer[pp].base.size(); i++){
			testHexes[testPlayer[pp].base.get(i).getHexNum()].occupation.setBase(i);
			setBaseHexes(pp, i);
		}
		
		checkBaseLink(p);
	}
	
	void sackBaseVerNew(int p, int b, int pp, int bb){
		int baseBig=0,base=0;
		
		if (!testPlayer[pp].isAI())
			score+=10;
		
		testPlayer[p].base.get(b).setMoney(testPlayer[pp].base.get(bb).getSavings());
		
		if (testPlayer[pp].base.get(bb).getHexesLinked() <= 1000){
			for (int h=0;h<hexVenture.BSIZE;h++){
				if ((testHexes[h].occupation.getPlayer() == pp)&&(testHexes[h].occupation.getBase()==bb)){
					testHexes[h].occupation.setPlayer(p);
					testHexes[h].occupation.setBase(b);
				}
			}
	
			killSackedBaseFighters(pp,bb);
	
			checkBaseLink(p);
			testHexes[testPlayer[pp].base.get(bb).getHexNum()].occupation.setOccupiedBy("empty");
			
			testPlayer[pp].base.remove(bb);
			
			for (int i = 0; i < hexVenture.BSIZE; i++)
				if (testHexes[i].occupation.getPlayer() == pp)
					testHexes[i].occupation.setBase(-1);
			
			for (int i = 0; i < testPlayer[pp].base.size(); i++){
				testHexes[testPlayer[pp].base.get(i).getHexNum()].occupation.setBase(i);
				setBaseHexes(pp, i);
			}
		}else{
			for (int n = 0; n < 6; n++)
				if (testHexes[testPlayer[pp].base.get(bb).getHexNum()].neighbor[n] > -1)
					if (testHexes[testHexes[testPlayer[pp].base.get(bb).getHexNum()].neighbor[n]].occupation.getPlayer() == pp)
						if (testHexes[testHexes[testPlayer[pp].base.get(bb).getHexNum()].neighbor[n]].occupation.getBase() == bb){
							testPlayer[pp].base.get(bb).setHexNum(testHexes[testPlayer[pp].base.get(bb).getHexNum()].neighbor[n]);
							break;
						}
			
			killSackedBaseFighters(pp,bb);
			testHexes[testPlayer[pp].base.get(bb).getHexNum()].occupation.setOccupiedBy("base");
			testPlayer[pp].base.get(bb).setSavings(0);
			testPlayer[pp].base.get(bb).setMoney(-testPlayer[pp].base.get(bb).getMoney());
		}
	}
	
	void setBaseHexes(int p, int b){
				int iWork=0;
				do{iWork++;
					int hex=0;
					int row=-1;
					int col=0;
					do{ row++;
						hex=row;
						do{ col++;
						if (!testHexes[hex].isReal())
							continue;
						if (testHexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
									if (testHexes[hex].neighbor[iii]==-1){
										testHexes[hex].line[iii] = b;
										continue;
									}
									if ((testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (testHexes[testHexes[hex].neighbor[iii]].isLinked())){
										testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
										testHexes[hex].line[iii] = -1;
									}else{
										testHexes[hex].line[iii] = b;
									}
								}
							}while(iii<5);
						}
						hex+=hexVenture.ROWSIZE;
						} while (col<hexVenture.COLSIZE);
					} while (row<(hexVenture.ROWSIZE-1));
				
					row=hexVenture.ROWSIZE;
					col=0;
					do{ row--;
						hex=row+((hexVenture.COLSIZE-1)*hexVenture.ROWSIZE);
						do{ col++;
						if (!testHexes[hex].isReal())
							continue;
						if (testHexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
									if (testHexes[hex].neighbor[iii]==-1){
										testHexes[hex].line[iii] = b;
										continue;
									}
									if ((testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (testHexes[testHexes[hex].neighbor[iii]].isLinked())){
										testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
										testHexes[hex].line[iii] = -1;
									}else{
										testHexes[hex].line[iii] = b;
									}
									if (!testHexes[testHexes[hex].neighbor[iii]].isReal())
										testHexes[hex].line[iii] = b;
								}
							}while(iii<5);
						}
						hex-=hexVenture.ROWSIZE;
						} while (col<hexVenture.COLSIZE);
					} while (row>0);
					
					//-------------------------------
					
					hex=-1;
					do{ hex++;
					if (!testHexes[hex].isReal())
						continue;
				    	if (testHexes[hex].occupation.getPlayer() == p){
				    		int iii=-1;
				    		do{iii++;
				    		if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
				    			if (testHexes[hex].neighbor[iii]==-1){
				    				testHexes[hex].line[iii] = b;
									continue;
								}
				    			if ((testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (testHexes[testHexes[hex].neighbor[iii]].isLinked())){
									testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
									testHexes[hex].line[iii] = -1;
				    			}else{
				    				testHexes[hex].line[iii] = b;
								}
				    			if (!testHexes[testHexes[hex].neighbor[iii]].isReal())
				    				testHexes[hex].line[iii] = b;
				    		}
				    		}while(iii<5);

				    	}
					} while (hex<(hexVenture.BSIZE)-1);
				
					hex=hexVenture.BSIZE;
					
					do{ hex--;
					if (!testHexes[hex].isReal())
						continue;
					if (testHexes[hex].occupation.getPlayer() == p){
			    		int iii=-1;
			    		do{iii++;
			    		if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
			    			if (testHexes[hex].neighbor[iii]==-1){
			    				testHexes[hex].line[iii] = b;
								continue;
							}
			    			if ((testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (testHexes[testHexes[hex].neighbor[iii]].isLinked())){
								testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
								testHexes[hex].line[iii] = -1;
			    			}else{
			    				testHexes[hex].line[iii] = b;
							}
			    			if (!testHexes[testHexes[hex].neighbor[iii]].isReal())
			    				testHexes[hex].line[iii] = b;
			    		}
			    		}while(iii<5);

			    	}
					
					} while (hex>0);
					
				}while(iWork<6);
	}
	
	void hexLink() { // determine linked hexes
		
		for (int hex = 0; hex < (hexVenture.BSIZE); hex++){
			//if (hexes[hex].occupation.getOccupiedBy() == "base")
				//continue;
			testHexes[hex].setLinked(false); 
		}
			
		for (int p = 0; p <= hexVenture.numPlayers; p++){
		for (int b = 0; b < testPlayer[p].base.size(); b++){
			
			testPlayer[p].base.get(b).setHexesLinked(-testPlayer[p].base.get(b).getHexesLinked());
			
		for (int hex = 0; hex < (hexVenture.BSIZE); hex++){
			testHexes[hex].setTallyLinked(false);
		}
		testHexes[testPlayer[p].base.get(b).getHexNum()].setTallyLinked(true);
		testHexes[testPlayer[p].base.get(b).getHexNum()].setLinked(true);
					
				int iWork=0;
				do{iWork++;
					int hex=0;
					int row=-1;
					int col=0;
					do{ row++;
						hex=row;
						do{ col++;
						if (!testHexes[hex].isReal())
							continue;
						if (testHexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
									if (testHexes[hex].neighbor[iii]==-1){
										testHexes[hex].line[iii] = b;
										continue;
									}
									if (testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										testHexes[testHexes[hex].neighbor[iii]].setTallyLinked(true);
										testHexes[testHexes[hex].neighbor[iii]].setLinked(true);
										if (testHexes[testHexes[hex].neighbor[iii]].occupation.getBase() == -1)
											testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
										testHexes[hex].line[iii] = -1;
									}else{
										testHexes[hex].line[iii] = b;
									}
								}
							}while(iii<5);
						}
						hex+=hexVenture.ROWSIZE;
						} while (col<hexVenture.COLSIZE);
					} while (row<(hexVenture.ROWSIZE-1));
				
					row=hexVenture.ROWSIZE;
					col=0;
					do{ row--;
						hex=row+((hexVenture.COLSIZE-1)*hexVenture.ROWSIZE);
						do{ col++;
						if (!testHexes[hex].isReal())
							continue;
						if (testHexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
									if (testHexes[hex].neighbor[iii]==-1){
										testHexes[hex].line[iii] = b;
										continue;
									}
									if (testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										testHexes[testHexes[hex].neighbor[iii]].setTallyLinked(true);
										testHexes[testHexes[hex].neighbor[iii]].setLinked(true);
										if (testHexes[testHexes[hex].neighbor[iii]].occupation.getBase() == -1)
											testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
										testHexes[hex].line[iii] = -1;
									}else{
										testHexes[hex].line[iii] = b;
									}
									if (!testHexes[testHexes[hex].neighbor[iii]].isReal())
										testHexes[hex].line[iii] = b;
								}
							}while(iii<5);
						}
						hex-=hexVenture.ROWSIZE;
						} while (col<hexVenture.COLSIZE);
					} while (row>0);
					
					//-------------------------------
					
					hex=-1;
					do{ hex++;
					if (!testHexes[hex].isReal())
						continue;
				    	if (testHexes[hex].occupation.getPlayer() == p){
				    		int iii=-1;
				    		do{iii++;
				    		if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
				    			if (testHexes[hex].neighbor[iii]==-1){
				    				testHexes[hex].line[iii] = b;
									continue;
								}
				    			if (testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
				    				testHexes[testHexes[hex].neighbor[iii]].setTallyLinked(true);
				    				testHexes[testHexes[hex].neighbor[iii]].setLinked(true);
				    				if (testHexes[testHexes[hex].neighbor[iii]].occupation.getBase() == -1)
				    					testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
				    				testHexes[hex].line[iii] = -1;
				    			}else{
				    				testHexes[hex].line[iii] = b;
								}
				    			if (!testHexes[testHexes[hex].neighbor[iii]].isReal())
				    				testHexes[hex].line[iii] = b;
				    		}
				    		}while(iii<5);

				    	}
					} while (hex<(hexVenture.BSIZE)-1);
				
					hex=hexVenture.BSIZE;
					
					do{ hex--;
					if (!testHexes[hex].isReal())
						continue;
					if (testHexes[hex].occupation.getPlayer() == p){
			    		int iii=-1;
			    		do{iii++;
			    		if ((testHexes[hex].isLinked())&&(testHexes[hex].occupation.getBase()==b)){
			    			if (testHexes[hex].neighbor[iii]==-1){
			    				testHexes[hex].line[iii] = b;
								continue;
							}
			    			if (testHexes[testHexes[hex].neighbor[iii]].occupation.getPlayer() == p){
			    				testHexes[testHexes[hex].neighbor[iii]].setTallyLinked(true);
			    				testHexes[testHexes[hex].neighbor[iii]].setLinked(true);
			    				if (testHexes[testHexes[hex].neighbor[iii]].occupation.getBase() == -1)
			    					testHexes[testHexes[hex].neighbor[iii]].occupation.setBase(b);
			    				testHexes[hex].line[iii] = -1;
			    			}else{
			    				testHexes[hex].line[iii] = b;
							}
			    			if (!testHexes[testHexes[hex].neighbor[iii]].isReal())
			    				testHexes[hex].line[iii] = b;
			    		}
			    		}while(iii<5);

			    	}
					
					if (iWork==5){
		    			if (testHexes[hex].occupation.getPlayer() == p){
		    				if (testHexes[hex].isTallyLinked()){
		    					if (testHexes[hex].occupation.getBase()==b)
		    						testPlayer[p].base.get(b).setHexesLinked(1);
		    				}
		    			}
		    		}
					
					} while (hex>0);
					
				}while(iWork<6);
				
				if (testPlayer[p].base.get(b).getHexesLinked() > 0)
					testPlayer[p].base.get(b).setRank(1);
				if (testPlayer[p].base.get(b).getHexesLinked() > hexVenture.BASE_LEVEL2_HEXES)
					testPlayer[p].base.get(b).setRank(2);
				if (testPlayer[p].base.get(b).getHexesLinked() > hexVenture.BASE_LEVEL3_HEXES)
					testPlayer[p].base.get(b).setRank(3);
		}
	
		}
	}
	
		void killSackedBaseFighters(int pp, int bb){
			while (testPlayer[pp].base.get(bb).fighter.size() > 0)
				killFighter(pp, bb, 0);
		}

		void killFighter(int p,int b,int f){
			int h = testPlayer[p].base.get(b).fighter.get(f).getHexNum();
			
			testHexes[h].setHasDeath(true);
			testHexes[h].occupation.setFighter(-1);
			testHexes[h].occupation.setFighterRank(1);
			testHexes[h].occupation.setPlayerDeath(p);
		
			testPlayer[p].base.get(b).fighter.remove(f);
		
		for (int i = 0; i < testPlayer[p].base.get(b).fighter.size(); i++)
			testHexes[testPlayer[p].base.get(b).fighter.get(i).getHexNum()].occupation.setFighter(i);
		}
		
		void placeFighterOnHex(int h, int p, int b, int f, int x, int y){
			testHexes[testPlayer[p].base.get(b).fighter.get(f).getHexNum()].occupation.setFighter(-1);
			testHexes[h].occupation.setOccupiedBy("empty");
			testHexes[h].setSetColor(testPlayer[p].getR(), testPlayer[p].getG(), testPlayer[p].getB());
			testPlayer[p].base.get(b).fighter.get(f).setHexNum(h);
			testPlayer[p].base.get(b).fighter.get(f).setX(x);
			testPlayer[p].base.get(b).fighter.get(f).setY(y);
			testHexes[h].occupation.setPlayer(p);
			testHexes[h].occupation.setFighter(f);
			testHexes[h].occupation.setFighterRank(testPlayer[p].base.get(b).fighter.get(f).getRank());
			testHexes[h].occupation.setBase(b);
			testPlayer[p].base.get(b).fighter.get(f).setMoved(true);
			testHexes[h].setHasDeath(false);
			testHexes[h].setHasFalseIncome(false);
			testHexes[h].setLinked(true);

			setFighterGridDefend(p, b, f, h);
			
			checkBaseLink(p);
			hexLink();
			checkDivision();
		}
		
		void checkBaseLink(int p){
			ArrayList<Integer> intBaseLinkList = new ArrayList<Integer>();
			boolean dup = false;
			for (int h = 0; h < hexVenture.BSIZE; h++){
				if (testHexes[h].occupation.getPlayer() != p)
					continue;
				if (testHexes[h].occupation.getBase() != hexVenture.selectedBase)
					continue;
				if (!testHexes[h].isLinked())
					continue;
				for (int n=0;n<6;n++)
					if (testHexes[h].neighbor[n]>=0)
						if (testHexes[testHexes[h].neighbor[n]].isLinked())
							if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer()==p)
								if ((testHexes[testHexes[h].neighbor[n]].occupation.getBase() != hexVenture.selectedBase) && (testHexes[testHexes[h].neighbor[n]].occupation.getBase() >= 0)){
									for (int i = 0; i < intBaseLinkList.size(); i++){
										if (intBaseLinkList.get(i) == testHexes[testHexes[h].neighbor[n]].occupation.getBase()){
											dup = true;
											break;
										}
									}
									if (!dup)
										intBaseLinkList.add(testHexes[testHexes[h].neighbor[n]].occupation.getBase());
									dup = false;
								}
				}
			
			if (intBaseLinkList.size() > 0)
				linkBases(p, intBaseLinkList, hexVenture.selectedBase);
		}
		
		void linkBases(int p, ArrayList<Integer> baseList, int bigb){
			int mainBase = bigb, inc;
			for (int b = 0; b < baseList.size(); b++){
				testPlayer[p].base.get(baseList.get(b)).setAlive(true);
				for (int h = 0; h < hexVenture.BSIZE; h++)
					if (testHexes[h].occupation.getPlayer() == p)
						if (testHexes[h].occupation.getBase() == baseList.get(b))
							testHexes[h].occupation.setBase(bigb);
			}
			
			testPlayer[p].base.get(bigb).setSelected(true);
			
			// if we are removing a base from testPlayer, then that will decrement amount, so then if we ask if an index from
			// baseList is alive, that base is now too high
			
			while (baseList.size() > 0){
				baseList.clear();
				for (int i = 0; i < testPlayer[p].base.size(); i++){
					if (testPlayer[p].base.get(i).isSelected())
						mainBase = i;
					if (testPlayer[p].base.get(i).isAlive())
						baseList.add(i);
				}
			
				inc = baseList.get(0);
	
				testPlayer[p].base.get(mainBase).fighter.addAll(testPlayer[p].base.get(inc).fighter);
				testPlayer[p].base.get(inc).fighter.clear();
				testHexes[testPlayer[p].base.get(inc).getHexNum()].occupation.setOccupiedBy("empty");
				if (testPlayer[p].base.get(inc).getRank()>testPlayer[p].base.get(mainBase).getRank())
					testPlayer[p].base.get(mainBase).setRank(testPlayer[p].base.get(inc).getRank());
				testPlayer[p].base.get(mainBase).setMoney(testPlayer[p].base.get(inc).getMoney());
				testPlayer[p].setBasesLinked(testPlayer[p].getBasesLinked()+1);
				testPlayer[p].base.get(inc).setAlive(false);
				testPlayer[p].base.remove(inc);
				baseList.remove(0);
			}
			
			for (int i = 0; i < testPlayer[p].base.size(); i++)
				if (testPlayer[p].base.get(i).isSelected())
					mainBase = i;

			for (int i = 0; i < hexVenture.BSIZE; i++)
				if (testHexes[i].occupation.getPlayer() == p)
					testHexes[i].occupation.setBase(-1);
			
			for (int i = 0; i < testPlayer[p].base.size(); i++){
				testHexes[testPlayer[p].base.get(i).getHexNum()].occupation.setBase(i);
				setBaseHexes(p, i);
				for (int f = 0; f < testPlayer[p].base.get(i).fighter.size(); f++)
					testHexes[testPlayer[p].base.get(i).fighter.get(f).getHexNum()].occupation.setFighter(f);
			}
			
			hexVenture.selectedBase = mainBase;
			testPlayer[p].base.get(mainBase).setSelected(false);
		}
		
		ArrayList<Integer> getNewBaseHexes(){
			ArrayList<Integer> intNewBaseHexList = new ArrayList<Integer>();
			ArrayList<Integer> intUnlinkedList = new ArrayList<Integer>();
			int player = -1, originHex = -1;
					
			Hex[] tempHexes = new Hex[hexVenture.BSIZE];
			for (int i = 0; i < hexVenture.hexes.length; i++){
				tempHexes[i] = new Hex();
				tempHexes[i].setReal(testHexes[i].isReal());
				tempHexes[i].setLinked(testHexes[i].isLinked());
				tempHexes[i].occupation = new hexOccupation();
				tempHexes[i].occupation.setPlayer(testHexes[i].occupation.getPlayer());
				tempHexes[i].occupation.setBase(testHexes[i].occupation.getBase());
				tempHexes[i].occupation.setFighter(testHexes[i].occupation.getFighter());
				for (int n = 0; n < 6; n++)
					tempHexes[i].neighbor[n] = testHexes[i].neighbor[n];
			}
			
			for (int i = 0; i < hexVenture.hexes.length; i++){
				if (!tempHexes[i].isReal)
					continue;
				if ((tempHexes[i].occupation.getBase() >= 0) && (!tempHexes[i].isLinked())){
					intUnlinkedList.add(i);
					player = testHexes[i].occupation.getPlayer();
					originHex = i;
				}
			}
			
			if (!intUnlinkedList.isEmpty())
				intNewBaseHexList.addAll(getNewBaseHexes(player, originHex, tempHexes, intUnlinkedList));
			else
				return intUnlinkedList;
			
			return intNewBaseHexList;
		}

		public ArrayList<Integer> getNewBaseHexes(int player, int originHex, Hex[] temphexes, ArrayList<Integer> intUnlinkedList ) {
			ArrayList<Integer> intNewBaseHexList = new ArrayList<Integer>();
			boolean newHex;
		
			temphexes[originHex].setNew(true);
			
			do{ newHex = false;
			for (int i = 0; i < intUnlinkedList.size(); i++){
				if (temphexes[intUnlinkedList.get(i)].isNew()){
					for (int n = 0; n < 6; n++){
						if (temphexes[intUnlinkedList.get(i)].neighbor[n] > -1){
							if ((temphexes[temphexes[intUnlinkedList.get(i)].neighbor[n]].occupation.getPlayer() == player) && (!temphexes[temphexes[intUnlinkedList.get(i)].neighbor[n]].isNew())){
								temphexes[temphexes[intUnlinkedList.get(i)].neighbor[n]].setNew(true);
								newHex = true;
							}
						}
					}
				}
			}
			}while (newHex);
			
			for (int i = 0; i < intUnlinkedList.size(); i++)
				if (temphexes[intUnlinkedList.get(i)].isNew())
					intNewBaseHexList.add(intUnlinkedList.get(i));
			
			return intNewBaseHexList;
		}
		
		void checkDivision(){
			ArrayList<Integer> intMoveList = new ArrayList<Integer>();
			int pl = -1;
			
			do{
				intMoveList.clear();
				intMoveList.addAll(getNewBaseHexes());
			
				if (intMoveList.isEmpty())
					return;
				
				pl = testHexes[intMoveList.get(0)].occupation.getPlayer();

				if (!testPlayer[pl].isAI())
					score+=intMoveList.size();
				
				if (intMoveList.size() <= 0){
					for (int i = 0 ; i < intMoveList.size(); i++){
						testHexes[intMoveList.get(i)].setSetColor(testPlayer[pl].getR()/2, testPlayer[pl].getG()/2, testPlayer[pl].getB()/2);
						if (testHexes[intMoveList.get(i)].occupation.getFighter() > -1)
							killFighter(pl, testHexes[intMoveList.get(i)].occupation.getBase(), testHexes[intMoveList.get(i)].occupation.getFighter());
					
						testHexes[intMoveList.get(i)].occupation.setBase(-1);
					}

					continue;
				}
				
				createNewBase(pl, intMoveList);
			
			}while (!intMoveList.isEmpty());
		}
		
	void createNewBase(int pl, ArrayList<Integer> intBaseHexList){
		int b = -1, oldb = -1;
		
		Base tempBase = new Base();
		testPlayer[pl].base.add(tempBase);
		b = testPlayer[pl].base.size()-1;
		oldb=testHexes[intBaseHexList.get(0)].occupation.getBase();                                                                                   
		
		testPlayer[pl].base.get(b).setHexNum(intBaseHexList.get(0));
						
		if (testHexes[testPlayer[pl].base.get(b).getHexNum()].occupation.getFighter() > -1)
			killFighter(pl, oldb, testHexes[testPlayer[pl].base.get(b).getHexNum()].occupation.getFighter());
		
		testPlayer[pl].base.get(b).setRank(1);
		testHexes[testPlayer[pl].base.get(b).getHexNum()].occupation.setBase(b);
		testHexes[testPlayer[pl].base.get(b).getHexNum()].occupation.setOccupiedBy("base");
		testPlayer[pl].base.get(b).setMoney(0);
		testPlayer[pl].base.get(b).setCol(testHexes[testPlayer[pl].base.get(b).getHexNum()].getColumn());
		testPlayer[pl].base.get(b).setRow(testHexes[testPlayer[pl].base.get(b).getHexNum()].getRow());
		hexLinkNewBase(pl, b, intBaseHexList);
				 
		splitFighters(pl, b, oldb, intBaseHexList);
	}
	
	void hexLinkNewBase(int p, int b, ArrayList<Integer> intNewBaseHexList) {
		for (int i = 0; i < intNewBaseHexList.size(); i++){
			testHexes[intNewBaseHexList.get(i)].setLinked(true);
			testHexes[intNewBaseHexList.get(i)].occupation.setBase(b);
		}
	}
		
	void splitFighters(int pl, int b, int oldb, ArrayList<Integer> intBaseHexList){
		for (int i = 0; i < intBaseHexList.size(); i++){
			if (testHexes[intBaseHexList.get(i)].occupation.getFighter() > -1){
				Fighter tempFighter = new Fighter();
				testPlayer[pl].base.get(b).fighter.add(tempFighter);
				int f = testPlayer[pl].base.get(b).fighter.size()-1;
				int oldf=testHexes[intBaseHexList.get(i)].occupation.getFighter();

				testPlayer[pl].base.get(b).fighter.get(f).setBaseNum(b);
				testPlayer[pl].base.get(b).fighter.get(f).setHexNum(intBaseHexList.get(i));
				testPlayer[pl].base.get(b).fighter.get(f).setRank(testPlayer[pl].base.get(oldb).fighter.get(oldf).getRank());
				testPlayer[pl].base.get(b).fighter.get(f).setCost(testPlayer[pl].base.get(oldb).fighter.get(oldf).getCost());
				testPlayer[pl].base.get(b).fighter.get(f).setX(testPlayer[pl].base.get(oldb).fighter.get(oldf).getX());
				testPlayer[pl].base.get(b).fighter.get(f).setY(testPlayer[pl].base.get(oldb).fighter.get(oldf).getY());
				testPlayer[pl].base.get(b).fighter.get(f).setMoved(testPlayer[pl].base.get(oldb).fighter.get(oldf).isMoved());
						
				killFighter(pl, oldb, oldf);
				testHexes[intBaseHexList.get(i)].setHasDeath(false);
				testHexes[intBaseHexList.get(i)].occupation.setFighterRank(testPlayer[pl].base.get(b).fighter.get(f).getRank());
				testHexes[intBaseHexList.get(i)].occupation.setFighter(f);
			}
		}
	}
	
	void fillBaseQueue(int p){
		testPlayer[p].intBaseQueue.clear();
		for (int b = 0; b < testPlayer[p].base.size(); b++){
			if (availableBaseMoves(p, b))
				testPlayer[p].intBaseQueue.add(b);	
			setBaseGridDefend(p, b);
		}
	}
	
	void fillBaseFighterQueue(int p, int b){
		testPlayer[p].base.get(b).intFighterQueue.clear();
		for (int f = 0; f < testPlayer[p].base.get(b).fighter.size(); f++){
			if (!testPlayer[p].base.get(b).fighter.get(f).isMoved()){
				testPlayer[p].base.get(b).intFighterQueue.add(f);
			}else{
				setFighterGridDefend(p, b, f, testPlayer[p].base.get(b).fighter.get(f).getHexNum());
			}
		}
	}
	
	boolean availableBaseMoves(int p, int b){
		for (int f = 0; f < testPlayer[p].base.get(b).fighter.size(); f++)
			if (!testPlayer[p].base.get(b).fighter.get(f).isMoved())
				return true;
		if (testPlayer[p].base.get(b).getMoney() >= (hexVenture.FIGHTER_RANK_ONE_EXPENSE*5) && (availableHighlight(p, b)))
			return true;
		
		return false;
	}
		
	void setBaseGridDefend(int p, int b){
		testHexes[testPlayer[p].base.get(b).getHexNum()].setDefendRank(testPlayer[p].base.get(b).getRank());
		
		for (int n = 0; n < 6; n++)
			if (testHexes[testPlayer[p].base.get(b).getHexNum()].neighbor[n] > -1)
				if ((testHexes[testHexes[testPlayer[p].base.get(b).getHexNum()].neighbor[n]].occupation.getPlayer() == p) && (testHexes[testHexes[testPlayer[p].base.get(b).getHexNum()].neighbor[n]].occupation.getBase() == b))
					testHexes[testHexes[testPlayer[p].base.get(b).getHexNum()].neighbor[n]].setDefendRank(testPlayer[p].base.get(b).getRank());
	}
	
	void setFighterGridDefend(int p, int b, int f, int h){
		testHexes[h].setDefendRank(testPlayer[p].base.get(b).fighter.get(f).getRank());
		testHexes[h].setFighterCover(true);
			
		for (int n=0;n<6;n++){
			if (testHexes[h].neighbor[n]>=0){
				if (testHexes[testHexes[h].neighbor[n]].occupation.getPlayer() == p){
					testHexes[testHexes[h].neighbor[n]].setFighterCover(true);
					if (testHexes[testHexes[h].neighbor[n]].getDefendRank() < testPlayer[p].base.get(b).fighter.get(f).getRank()){
						testHexes[testHexes[h].neighbor[n]].setDefendRank(testPlayer[p].base.get(b).fighter.get(f).getRank());
					}
				}
			
				for (int nn=0;nn<6;nn++)
					if (testHexes[testHexes[h].neighbor[n]].neighbor[nn] >= 0)
						if (testHexes[testHexes[testHexes[h].neighbor[n]].neighbor[nn]].occupation.getPlayer() == p)
							testHexes[testHexes[testHexes[h].neighbor[n]].neighbor[nn]].setFighterCover(true);
			}
		}
	}
	
	void clearGridRanks(){
		for (int i = 0; i < hexVenture.BSIZE; i++){
			testHexes[i].setDefendRank(0);
			testHexes[i].setSumRank(0);
			testHexes[i].setThreatRank(0);
			testHexes[i].setFighterCover(false);
		}
	}
	
	void setSumRanks(){
		for (int h = 0; h < hexVenture.BSIZE; h++){
			testHexes[h].setSumRank(testHexes[h].getDefendRank() - testHexes[h].getThreatRank());
		
			if (testHexes[h].getSumRank() > 0)
				testHexes[h].setSumRank(0);
		}
	}
	
	int getMoveScore(int p, int b, int f){
		if (!intEnemyBaseTerList.isEmpty())
			return 10;
		
		if (!intEnemyTerDivList.isEmpty())
			return 9;
		
		if (!intBaseLinkTerList.isEmpty())
			return 8;
		
		if (!intEnemyBaseNeighborTerList.isEmpty())
			return 7;
		
		if (!intEnemyFighterTerList.isEmpty())
			return 6;
		
		if (!intEnemyTerDivNeighborList.isEmpty())
			return 5;
		
		if (!intEnemyTerList.isEmpty())
			return 4;
		
		if (!intEnemyFighterNeighborTerList.isEmpty())
			return 3;
		
		if (!intNeutralTerList.isEmpty())
			return 2;

		if (!intOwnFalseTerList.isEmpty())
			return 1;
		
		if (!intAllOwnTerList.isEmpty())
			return 1;
		
		return -1;
	}
}
