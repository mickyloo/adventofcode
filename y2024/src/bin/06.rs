use std::collections::HashSet;
use anyhow::*;
use std::fs::File;
use std::io::{BufRead, BufReader};
use code_timing_macros::time_snippet;
use const_format::concatcp;
use adv_code_2024::*;

const DAY: &str = "06";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...";

#[derive(Eq, Hash, PartialEq, Clone)]
enum DIR {
    LEFT, RIGHT, UP, DOWN
}


fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let grid = read_to_2d(reader);
        let start = find(grid.clone(), '^');
        let visited = patrol(start, grid);
        let answer = visited.len();
        Ok(answer)
    }
    fn patrol(start: (usize, usize), grid: Vec<Vec<char>>) -> HashSet<(i32, i32)> {
        let mut visited: HashSet<(i32, i32)> = HashSet::new();
        visited.insert((start.0 as i32, start.1 as i32));
        let mut direction = DIR::UP;
        let mut curr = (start.0 as i32, start.1 as i32);
        loop {
            visited.insert(curr);

            let new_curr = match direction {
                DIR::UP => (curr.0 - 1, curr.1),
                DIR::LEFT => (curr.0, curr.1 - 1),
                DIR::RIGHT => (curr.0, curr.1 + 1),
                DIR::DOWN => (curr.0 + 1, curr.1)
            };

            // break if OOB
            if new_curr.0 < 0 || new_curr.0 >= grid.len() as i32 || new_curr.1 < 0 || new_curr.1 >= grid[0].len() as i32 {
                break;
            }

            // check value at new square
            let value = grid[new_curr.0 as usize][new_curr.1 as usize];
            if value == '#' {
                direction = match direction {
                    DIR::UP => DIR::RIGHT,
                    DIR::LEFT => DIR::UP,
                    DIR::RIGHT => DIR::DOWN,
                    DIR::DOWN => DIR::LEFT
                };
            } else {
                curr = new_curr;
            }
        }
        visited
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(41, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let _grid = read_to_2d(reader);
        let start = find(_grid.clone(), '^');
        let mut add_obstacles = patrol(start, _grid.clone());
        add_obstacles.remove(&(start.0 as i32, start.1 as i32));

        let mut answer = 0;
        // add obstacles at visited
        for obstacle in add_obstacles {
            let mut grid = _grid.clone();
            grid[obstacle.0 as usize][obstacle.1 as usize] = '#';

            let mut visited: HashSet<(i32, i32, DIR)> = HashSet::new();
            let mut direction = DIR::UP;
            let mut curr = (start.0 as i32, start.1 as i32, direction.clone());
            loop {
                visited.insert((curr.0, curr.1, direction.clone()));

                let new_curr = match direction {
                    DIR::UP => (curr.0 - 1, curr.1, DIR::UP),
                    DIR::LEFT => (curr.0, curr.1 - 1, DIR::LEFT),
                    DIR::RIGHT => (curr.0, curr.1 + 1, DIR::RIGHT),
                    DIR::DOWN => (curr.0 + 1, curr.1, DIR::DOWN),
                };

                // break if OOB
                if new_curr.0 < 0 || new_curr.0 >= grid.len() as i32 || new_curr.1 < 0 || new_curr.1 >= grid[0].len() as i32 {
                    break;
                }
                if visited.contains(&new_curr) {
                    answer += 1;
                    break;
                }

                // check value at new square
                let value = grid[new_curr.0 as usize][new_curr.1 as usize];
                if value == '#' {
                    direction = match direction {
                        DIR::UP => DIR::RIGHT,
                        DIR::LEFT => DIR::UP,
                        DIR::RIGHT => DIR::DOWN,
                        DIR::DOWN => DIR::LEFT
                    };
                    curr = (curr.0, curr.1, direction.clone());
                } else {
                    curr = new_curr;
                }
            }
        }

        Ok(answer)
    }

    assert_eq!(6, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
