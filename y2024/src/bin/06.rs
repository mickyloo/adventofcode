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

const TEST_START: (i32, i32) = (6, 4);
const START: (i32, i32) = (54, 56);

enum DIR {
    LEFT, RIGHT, UP, DOWN
}

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R, start: (i32, i32)) -> Result<usize> {
        let grid = read_to_2d(reader);

        let mut visited: HashSet<(i32, i32)> = HashSet::new();
        visited.insert(start);
        let mut direction = DIR::UP;
        let mut curr = start.clone();
        loop {
            visited.insert(curr);

            let new_curr = match direction {
                DIR::UP => (curr.0 -1, curr.1),
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
        let answer = visited.len();
        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(41, part1(BufReader::new(TEST.as_bytes()), TEST_START)?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file, START)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
