# Checkers

This program is a neat implementation of the Checkers game with a simple GUI and two AI variations.
User can choose to play against the computer player that implements either a Random algorithm or a Minimax AI algorithm. The Minimax AI uses optimal moves for the computer which makes it a more formidable opponent as compared to the Random AI. The winner of the game is one who can make the last move; that is, no move is available to the opponent on their turn to play, either because all their pieces have been captured or their remaining pieces are all blocked.
When the depth of the Minimax tree is chosen as 1, it behaves randomly.


Random:  If the computer can capture the player’s man then it does, otherwise plays a random move.
Minimax: At each turn computer chooses the optimal move for itself i.e. the worst move for the opponent. This is implemented using the Minimax Algorithm.

Working game:
Red: User
Black: Computer

![image](https://user-images.githubusercontent.com/54510650/170855879-3753f570-bfca-412f-818f-e0f10d76ddcc.png)
Red Has a chance to capture black

![image](https://user-images.githubusercontent.com/54510650/170855897-fec8df9a-09eb-4cf7-95a6-bbf4eaebb8d9.png)
Red captures Black

